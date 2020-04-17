package com.snowmanlabs.test.router

import android.app.Activity
import com.snowmanlabs.test.presentation.HomeActivity

class LoginRouter(private val activity: Activity) {
    fun navigate() {
        HomeActivity.launch(activity)
    }

    fun finish() {
        activity.finish()
    }
}