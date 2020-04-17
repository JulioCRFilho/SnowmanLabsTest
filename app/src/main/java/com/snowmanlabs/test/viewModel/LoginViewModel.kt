package com.snowmanlabs.test.viewModel

import android.app.Activity
import android.content.Context
import android.util.Log.d
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.snowmanlabs.test.interactor.LoginInterface
import com.snowmanlabs.test.model.api.Firebase

class LoginViewModel : ViewModel(), LoginInterface.Router {
    val facebookCallback: CallbackManager = CallbackManager.Factory.create()
    private val loginManager = LoginManager.getInstance()

    lateinit var viewInterface: LoginInterface.View

    val viewStatus = MutableLiveData<Pair<Int, String?>>(Pair(0, null))

    fun signInWithFacebook(view: View) {
        viewInterface.loading(loginManager)

        loginManager.registerCallback(facebookCallback, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                handleFacebookAccessToken(result?.accessToken)
            }

            override fun onCancel() {
                viewInterface.error("Solicitação cancelada")
            }

            override fun onError(error: FacebookException?) {
                error?.message?.let { msg ->
                    viewInterface.error(msg)
                }
            }
        })
    }

    fun verifyUserLoggedIn(context: Context) {
        Firebase.user?.let {
            d("tatata", Firebase.user.email ?: "nada")
            this.navigate(context)
        }
    }

    private fun handleFacebookAccessToken(accessToken: AccessToken?) {
        d("tatata", "handling")

        accessToken?.let { access ->
            val credential = FacebookAuthProvider.getCredential(access.token)

            Firebase.mAuth.signInWithCredential(credential).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    viewInterface.success()
                } else {
                    task.exception?.message?.let { msg ->
                        viewInterface.error(msg)
                    }
                }
            }
        }
    }
}