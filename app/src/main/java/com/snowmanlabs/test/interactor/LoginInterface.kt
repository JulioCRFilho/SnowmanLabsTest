package com.snowmanlabs.test.interactor

import android.app.Activity
import android.content.Context
import androidx.navigation.NavController
import com.facebook.login.LoginManager
import com.snowmanlabs.test.R
import com.snowmanlabs.test.presentation.HomeFragment

interface LoginInterface {
    interface View {
        fun loading(loginManager: LoginManager)
        fun error(msg: String)
        fun success()
    }

    interface Router {
        fun navigate(navController: NavController) {
            navController.navigate(R.id.actionUserLogged)
        }
    }
}