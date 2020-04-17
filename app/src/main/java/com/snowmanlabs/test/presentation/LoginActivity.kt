package com.snowmanlabs.test.presentation

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log.d
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.facebook.login.LoginManager
import com.snowmanlabs.test.R
import com.snowmanlabs.test.databinding.ActivityLoginBinding
import com.snowmanlabs.test.interactor.LoginInterface
import com.snowmanlabs.test.model.api.Firebase
import com.snowmanlabs.test.utils.CustomDialog
import com.snowmanlabs.test.viewModel.LoginViewModel

class LoginActivity : AppCompatActivity(), LoginInterface.View {
    lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        binding.viewModel = viewModel
        viewModel.viewInterface = this
    }

    override fun loading(loginManager: LoginManager) {
        loginManager.logInWithReadPermissions(this, listOf("public_profile", "user_friends"))
    }

    override fun error(msg: String) {
        viewModel.viewStatus.value = Pair(1, msg)
        CustomDialog(this, viewModel.viewStatus, this).show()
    }

    override fun success() {
        Firebase.getUser().let {
            viewModel.navigate(this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        viewModel.facebookCallback.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}
