package com.snowmanlabs.test.model.entity

import android.graphics.Bitmap
import java.io.Serializable

data class CustomLocation(
    var id: String? = null,
    var name: String? = null,
    var address: String? = null,
    var rating: Double? = null,
    var photo: Bitmap? = null
): Serializable