package com.snowmanlabs.test.utils

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.multidex.MultiDex
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.snowmanlabs.test.R

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
    }
}