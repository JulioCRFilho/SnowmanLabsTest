package com.snowmanlabs.test.presentation

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.snowmanlabs.test.R
import com.snowmanlabs.test.model.api.Firebase

class SplashFragment : Fragment() {
    override fun onCreateView(i: LayoutInflater, v: ViewGroup?, s: Bundle?): View? {
        return i.inflate(R.layout.fragment_splash, v, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler().postDelayed({
            if (Firebase.getUser() != null) {
                findNavController().navigate(R.id.actionUserLogged)
            } else {
                findNavController().navigate(R.id.actionLogin)
            }
        }, 1000)
    }
}