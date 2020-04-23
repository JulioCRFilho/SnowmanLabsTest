package com.snowmanlabs.test.viewModel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.snowmanlabs.test.interactor.ProfileInterface
import com.snowmanlabs.test.model.api.Firebase

class ProfileViewModel : ViewModel(), ProfileInterface.Router {
    lateinit var interactor: ProfileInterface.UI
    val user = Firebase.getUser()
    val status = MutableLiveData<Pair<Int, String?>>()

    fun userLogout(view: View) {
        Firebase.loggout()
        logout(view.findNavController())
    }

    fun userChangePass(view: View) {
        interactor.onLoading()

        if (!user?.email.isNullOrEmpty()) {
            Firebase.mAuth.sendPasswordResetEmail(user?.email!!).addOnCompleteListener {
                if (it.isSuccessful) {
                    interactor.onSuccess("Solicitação encaminhada para o email ${user.email}")
                } else {
                    it.exception?.message?.let { msg -> interactor.onError(msg) }
                }
            }.addOnFailureListener {
                it.message?.let { msg -> interactor.onError(msg) }
            }
        } else {
            interactor.onError("Email não registrado")
        }
    }
}