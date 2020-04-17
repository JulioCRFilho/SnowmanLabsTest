package com.snowmanlabs.test.interactor

import android.app.Activity
import android.content.Context
import com.facebook.login.LoginManager
import com.snowmanlabs.test.presentation.HomeActivity

interface LoginInterface {
    interface View {
        fun loading(loginManager: LoginManager)
        fun error(msg: String)
        fun success()
    }

    interface Router {
        fun navigate(context: Context) {
            HomeActivity.launch(context)
            (context as Activity).finish()
        }
    }
}