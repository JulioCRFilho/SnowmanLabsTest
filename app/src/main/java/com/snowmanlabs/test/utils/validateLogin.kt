package com.snowmanlabs.test.utils

import androidx.navigation.NavController
import com.snowmanlabs.test.R
import com.snowmanlabs.test.model.api.Firebase

fun validateLogin(navController: NavController): Boolean {
    return if(Firebase.getUser() != null) {
        true
    } else {
        navController.navigate(R.id.actionLogin)
        false
    }
}