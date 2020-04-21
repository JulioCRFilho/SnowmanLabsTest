package com.snowmanlabs.test.viewModel

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.util.Log.d
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.snowmanlabs.test.R
import com.snowmanlabs.test.interactor.MapInterface
import kotlinx.android.synthetic.main.fragment_map.*

class MapViewModel : ViewModel() {
    val ACCESS_CODE = 101
    val searchQuery = MutableLiveData("")
    val resultLiveData = MutableLiveData<List<String>>()

    private val token = AutocompleteSessionToken.newInstance()

    lateinit var myLoc: Location
    lateinit var mapInterface: MapInterface

    private val autoCompleteLocation = FindAutocompletePredictionsRequest.builder()
        .setSessionToken(token)
        .setTypeFilter(TypeFilter.REGIONS)

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
                    myLoc = result
                    val latlng = LatLng(myLoc.latitude, myLoc.longitude)
                    mMap.addMarker(MarkerOptions().position(latlng).title("You're here!"))
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 17f))
                }
            }
        }
    }

    fun autoCompleteSearch(context: Context, owner: LifecycleOwner) {
        var autoComplete: FindAutocompletePredictionsRequest
        Places.initialize(context, context.getString(R.string.google_api_key))
        val places = Places.createClient(context)

        searchQuery.observe(owner, Observer { local ->
            autoComplete = autoCompleteLocation.setQuery(local).build()

            places.findAutocompletePredictions(autoComplete)
                .addOnSuccessListener { response ->
                    if (response.autocompletePredictions.isNotEmpty()) {
                        val list = response.autocompletePredictions.map {
                            it.getPrimaryText(null).toString()
                        }
                        resultLiveData.value = list
                    }
                }.addOnFailureListener { exception ->
                    exception.message?.let { msg -> mapInterface.onError(msg) }
                }
        })
    }
}