package com.snowmanlabs.test.presentation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.facebook.login.LoginManager
import com.snowmanlabs.test.R
import com.snowmanlabs.test.databinding.FragmentLoginBinding
import com.snowmanlabs.test.interactor.LoginInterface
import com.snowmanlabs.test.model.api.Firebase
import com.snowmanlabs.test.utils.InteractorDialog
import com.snowmanlabs.test.utils.bottomBar
import com.snowmanlabs.test.viewModel.LoginViewModel

class LoginFragment : Fragment(), LoginInterface.UI {
    lateinit var viewModel: LoginViewModel
    private lateinit var binding: FragmentLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    override fun onCreateView(i: LayoutInflater, v: ViewGroup?, s: Bundle?): View? {
        bottomBar(false)
        binding = DataBindingUtil.inflate(i, R.layout.fragment_login, v, false)
        binding.viewModel = viewModel
        viewModel.viewInterface = this
        return binding.root
    }

    override fun loading(loginManager: LoginManager) {
        loginManager.logInWithReadPermissions(this, listOf("email", "public_profile"))
    }

    override fun error(msg: String) {
        InteractorDialog(requireContext(), msg).show()
    }

    override fun success() {
        Firebase.getUser().let {
            viewModel.navigate(findNavController())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        viewModel.facebookCallback.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}
