package com.snowmanlabs.test.viewModel

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
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
import com.google.android.libraries.places.api.model.*
import com.google.android.libraries.places.api.net.*
import com.snowmanlabs.test.R
import com.snowmanlabs.test.interactor.MapInterface
import com.snowmanlabs.test.model.entity.CustomLocation

class MapViewModel : ViewModel() {
    val ACCESS_CODE = 101
    val searchQuery = MutableLiveData("")
    val resultLiveData = MutableLiveData<List<AutocompletePrediction>>()
    var selectedLocation: CustomLocation? = null
    lateinit var places: PlacesClient

    private val token = AutocompleteSessionToken.newInstance()

    lateinit var myLoc: Location
    lateinit var mapInterface: MapInterface

    private val autoCompleteLocation = FindAutocompletePredictionsRequest
        .builder()
        .setSessionToken(token)

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
                    setMarker(mMap, latlng, "You're here!")
                    moveCamera(mMap, latlng)
                }
            }
        }
    }

    private fun setMarker(mMap: GoogleMap, latLng: LatLng, title: String? = "") {
        mMap.addMarker(MarkerOptions().position(latLng).title(title))
    }

    private fun moveCamera(mMap: GoogleMap, latLng: LatLng) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f))
    }

    fun autoCompleteSearch(context: Context, owner: LifecycleOwner) {
        var autoComplete: FindAutocompletePredictionsRequest
        Places.initialize(context, context.getString(R.string.google_api_key))
        places = Places.createClient(context)

        searchQuery.observe(owner, Observer { local ->
            autoComplete = autoCompleteLocation.setQuery(local).build()

            places.findAutocompletePredictions(autoComplete)
                .addOnSuccessListener { response ->
                    resultLiveData.value = response.autocompletePredictions
                }.addOnFailureListener { exception ->
                    exception.message?.let { msg -> mapInterface.onError(msg) }
                }
        })
    }

    fun fetchPlace(id: String, mMap: GoogleMap) {
        val fields = listOf(
            Place.Field.NAME,
            Place.Field.ADDRESS,
            Place.Field.PHOTO_METADATAS,
            Place.Field.LAT_LNG,
            Place.Field.RATING
        )
        val fetch = FetchPlaceRequest.newInstance(id, fields)

        places.fetchPlace(fetch).addOnCompleteListener {
            if (it.isSuccessful) {
                it.result?.place?.let { place ->
                    selectedLocation = CustomLocation(
                        place.id,
                        place.name,
                        place.address,
                        place.rating,
                        place.photoMetadatas?.get(0)?.let { it1 -> fetchPhoto(it1) }
                    )

                    place.latLng?.let { latLng ->
                        setMarker(mMap, latLng, place.name)
                        moveCamera(mMap, latLng)
                    }
                }
            } else {
                it.exception?.message?.let { msg -> mapInterface.onError(msg) }
            }
        }.addOnFailureListener { exception ->
            exception.message?.let { msg -> mapInterface.onError(msg) }
        }
    }

    private fun fetchPhoto(meta: PhotoMetadata): Bitmap? {
        var bitmap: Bitmap? = null
        val fetch = FetchPhotoRequest.newInstance(meta)
        places.fetchPhoto(fetch).addOnCompleteListener {
            if (it.isSuccessful) {
                bitmap = it.result?.bitmap
            } else {
                it.exception?.message?.let { msg -> mapInterface.onError(msg) }
            }
        }.addOnFailureListener { exception ->
            exception.message?.let { msg -> mapInterface.onError(msg) }
        }
        return bitmap
    }
}