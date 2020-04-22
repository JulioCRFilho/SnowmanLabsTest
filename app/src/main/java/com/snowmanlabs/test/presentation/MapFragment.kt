package com.snowmanlabs.test.presentation

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.material.bottomnavigation.BottomNavigationMenu
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.snowmanlabs.test.R
import com.snowmanlabs.test.databinding.FragmentMapBinding
import com.snowmanlabs.test.interactor.MapInterface
import com.snowmanlabs.test.presentation.adapter.AutoCompleteAdapter
import com.snowmanlabs.test.utils.CustomDialog
import com.snowmanlabs.test.utils.bottomBar
import com.snowmanlabs.test.viewModel.MapViewModel
import kotlinx.android.synthetic.main.fragment_map.*

class MapFragment : Fragment(), OnMapReadyCallback, MapInterface {
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
                    val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view?.windowToken, 0)
                    viewModel.fetchPlace(id, mMap)
                }
            }
        })
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
        CustomDialog(requireContext(), msg).show()
    }
}
