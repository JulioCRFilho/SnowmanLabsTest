package com.snowmanlabs.test.model.api

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

object Firebase {
    val mAuth = FirebaseAuth.getInstance()

    fun getUser(): FirebaseUser? {
        return mAuth.currentUser
    }

    fun loggout() {
        mAuth.signOut()
    }
}