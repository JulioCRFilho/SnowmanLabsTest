package com.snowmanlabs.test.presentation

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.snowmanlabs.test.R
import com.snowmanlabs.test.databinding.FragmentMapBinding
import com.snowmanlabs.test.interactor.MapInterface
import com.snowmanlabs.test.presentation.adapter.AutoCompleteAdapter
import com.snowmanlabs.test.utils.InteractorDialog
import com.snowmanlabs.test.utils.MapInfoDialog
import com.snowmanlabs.test.utils.bottomBar
import com.snowmanlabs.test.viewModel.MapViewModel
import kotlinx.android.synthetic.main.fragment_map.*

class MapFragment : Fragment(), OnMapReadyCallback, MapInterface.UI {
    private val viewModel by navGraphViewModels<MapViewModel>(R.id.nav_graph)
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var binding: FragmentMapBinding
    private lateinit var mMap: GoogleMap

    override fun onCreateView(i: LayoutInflater, v: ViewGroup?, s: Bundle?): View? {
        bottomBar(true)
        binding = DataBindingUtil.inflate(i, R.layout.fragment_map, v, false)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val nav = activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation)
        nav?.selectedItemId = R.id.icMap

        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        viewModel.getLocationPermission(this, mMap)
        viewModel.autoCompleteSearch(requireContext(), this)

        viewModel.resultLiveData.observe(this, Observer { list ->
            with(recyclerView) {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
                adapter = AutoCompleteAdapter(list) { id ->
                    searchField.setText("")
                    val imm =
                        context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view?.windowToken, 0)
                    viewModel.fetchPlace(id, mMap)
                }
            }
        })

        mMap.setOnMarkerClickListener { marker ->
            if (marker.id != "m0") {
                viewModel.selectedLocation.find { it?.flag == marker.id }?.let {
                    searchBarView.visibility = View.GONE

                    MapInfoDialog(requireContext(), it, findNavController(), this) {
                        searchBarView.visibility = View.VISIBLE
                    }.show()
                }

            } else {
                marker.showInfoWindow()
            }
            return@setOnMarkerClickListener true
        }
    }

    override fun onRequestPermissionsResult(r: Int, p: Array<out String>, g: IntArray) {
        super.onRequestPermissionsResult(r, p, g)
        if (r == viewModel.ACCESS_CODE) {
            if (g.count() > 0 && g[0] == PackageManager.PERMISSION_GRANTED) {
                activity?.let { viewModel.getInitialMark(it, mMap) }
            }
        }
    }

    override fun onError(msg: String) {
        InteractorDialog(requireContext(), msg).show()
    }

    override fun onLoading() {
        viewModel.statusLiveData.value = Pair(1, null)
        InteractorDialog(requireContext(), owner = this, status = viewModel.statusLiveData)
    }

    override fun onSuccess(msg: String) {
        viewModel.statusLiveData.value = Pair(2, msg)
    }

    override fun onFailure(msg: String) {
        viewModel.statusLiveData.value = Pair(0, msg)
    }
}
