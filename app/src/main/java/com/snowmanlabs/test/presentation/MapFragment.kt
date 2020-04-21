package com.snowmanlabs.test.presentation

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.snowmanlabs.test.R
import com.snowmanlabs.test.databinding.FragmentMapBinding
import com.snowmanlabs.test.utils.bottomBar
import com.snowmanlabs.test.viewModel.MapViewModel

class MapFragment : Fragment(), OnMapReadyCallback {
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var mMap: GoogleMap
    private lateinit var viewModel: MapViewModel
    private lateinit var binding: FragmentMapBinding

    override fun onCreateView(i: LayoutInflater, v: ViewGroup?, s: Bundle?): View? {
        bottomBar(true)
        viewModel = ViewModelProvider(this).get(MapViewModel::class.java)
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
    }

    override fun onRequestPermissionsResult(r: Int, p: Array<out String>, g: IntArray) {
        super.onRequestPermissionsResult(r, p, g)
        if (r == viewModel.ACCESS_CODE) {
            if (g.count() > 0 && g[0] == PackageManager.PERMISSION_GRANTED) {
                activity?.let { viewModel.getInitialMark(it, mMap) }
            }
        }
    }
}
