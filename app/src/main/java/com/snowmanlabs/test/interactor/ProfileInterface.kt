package com.snowmanlabs.test.interactor

import androidx.navigation.NavController

interface ProfileInterface {
    interface UI {
        fun onLoading()
        fun onError(msg: String)
        fun onSuccess(msg: String? = null)
    }

    interface Router {
        fun logout(navController: NavController) {
            navController.navigateUp()
        }
    }
}