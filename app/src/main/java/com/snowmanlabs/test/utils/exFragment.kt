package com.snowmanlabs.test.utils

import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.snowmanlabs.test.R

fun Fragment.bottomBar(visible: Boolean) {
    activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation)?.isVisible = visible
}