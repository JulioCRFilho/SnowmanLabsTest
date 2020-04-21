package com.snowmanlabs.test.viewModel

import android.app.Activity
import android.content.pm.PackageManager
import android.util.Log.d
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapViewModel : ViewModel() {
    val ACCESS_CODE = 101

    fun getLocationPermission(fragment: Fragment, mMap: GoogleMap) {
        if (ContextCompat.checkSelfPermission(
                fragment.requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            getInitialMark(fragment.requireActivity(), mMap)
        } else {
            fragment.requestPermissions(
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                ACCESS_CODE
            )
        }
    }

    fun getInitialMark(activity: Activity, mMap: GoogleMap) {
        val location = LocationServices.getFusedLocationProviderClient(activity).lastLocation
        location.addOnCompleteListener {
            if (it.isSuccessful) {
                it.result?.let { result ->
                    val myLoc = LatLng(result.latitude, result.longitude)
                    mMap.addMarker(MarkerOptions().position(myLoc).title("You're here!"))
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLoc, 15f))
                }
            }
        }
    }
}