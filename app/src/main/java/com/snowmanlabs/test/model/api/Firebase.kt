package com.snowmanlabs.test.model.api

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

object Firebase {
    val mAuth= FirebaseAuth.getInstance()
    val user: FirebaseUser? = mAuth.currentUser

    fun loggout() {
        mAuth.signOut()
    }
}