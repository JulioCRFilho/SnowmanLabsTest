package com.snowmanlabs.test.interactor

import androidx.navigation.NavController
import com.facebook.login.LoginManager
import com.snowmanlabs.test.R

interface LoginInterface {
    interface UI {
        fun loading(loginManager: LoginManager)
        fun error(msg: String)
        fun success()
    }

    interface Router {
        fun navigate(navController: NavController) {
            navController.navigate(R.id.actionProfileView)
        }
    }
}