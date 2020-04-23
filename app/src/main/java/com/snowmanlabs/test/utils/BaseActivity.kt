package com.snowmanlabs.test.utils

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.multidex.MultiDex
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.snowmanlabs.test.R
import kotlinx.android.synthetic.main.activity_base.*

class BaseActivity : AppCompatActivity() {
    lateinit var navController: NavController

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        MultiDex.install(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        navController = findNavController(R.id.nav_host_fragment_container)

        bottom_navigation.selectedItemId = R.id.icMap

        bottom_navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.icStar -> navController.navigate(R.id.actionFavView)
                R.id.icUser -> validateUser()
                R.id.icMap -> navController.navigateUp()
            }
            return@setOnNavigationItemSelectedListener true
        }

        bottom_navigation.setOnNavigationItemReselectedListener { }
    }

    private fun validateUser() {
        validateLogin(navController).let { user ->
            if (user) navController.navigate(R.id.actionProfileView)
        }
    }

    override fun onBackPressed() {
        if (bottom_navigation.selectedItemId == R.id.icMap) {
            super.onBackPressed()
        } else {
            bottom_navigation.selectedItemId = R.id.icMap
        }
    }
}