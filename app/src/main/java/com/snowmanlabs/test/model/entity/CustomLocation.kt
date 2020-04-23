package com.snowmanlabs.test.model.entity

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import com.google.android.libraries.places.api.model.Place
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CustomLocation(
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("address")
    var address: String? = null,
    @SerializedName("rating")
    var rating: Double? = null,
    @SerializedName("photo")
    var photo: Bitmap? = null,
    @SerializedName("flag")
    var flag: String? = null,
    @SerializedName("category")
    var category: MutableList<Place.Type>? = null,
    @SerializedName("favorite")
    var favorite: MutableLiveData<Boolean> = MutableLiveData(false)
): Serializable