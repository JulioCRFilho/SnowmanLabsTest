package com.snowmanlabs.test.presentation

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.snowmanlabs.test.R
import com.snowmanlabs.test.utils.bottomBar

class SplashFragment : Fragment() {
    override fun onCreateView(i: LayoutInflater, v: ViewGroup?, s: Bundle?): View? {
        bottomBar(false)
        return i.inflate(R.layout.fragment_splash, v, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler().postDelayed({
            findNavController().navigate(R.id.actionMapView)
        }, 1000)
    }
}