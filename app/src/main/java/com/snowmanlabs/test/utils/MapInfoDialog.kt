package com.snowmanlabs.test.utils

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.snowmanlabs.test.R
import com.snowmanlabs.test.model.entity.CustomLocation
import kotlinx.android.synthetic.main.dialog_map_info.*

class MapInfoDialog(
    context: Context,
    private val place: CustomLocation,
    private val navController: NavController,
    private val lifecycleOwner: LifecycleOwner,
    private val closeInfo: () -> Unit
) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_map_info)

        window?.attributes?.height = WindowManager.LayoutParams.MATCH_PARENT
        window?.attributes?.width = WindowManager.LayoutParams.MATCH_PARENT
        window?.setBackgroundDrawableResource(android.R.color.transparent)

        this.setCancelable(false)

        infoTitle.text = place.name
        infoSubtitle.text = place.address

        place.photo?.let { photo ->
            infoPic.setImageBitmap(photo)
        }

        val red = context.getColor(android.R.color.holo_red_light)
        val gray = context.getColor(android.R.color.darker_gray)

        place.favorite.observe(lifecycleOwner, Observer { value ->
            infoFavorite.drawable.setTint(if (value) red else gray)
            infoFavorite.setOnClickListener {
                validateLogin(navController).let { user ->
                    if (user) place.favorite.value = !place.favorite.value!! else dismiss()
                }
            }
        })

        infoCategory.text = place.category?.joinToString(", ") { it.name }?.toLowerCase()

        place.rating?.let { value ->
            infoRating.rating = value.toFloat()
        }

        closeBtn.setOnClickListener {
            this.dismiss()
            closeInfo()
        }
    }
}