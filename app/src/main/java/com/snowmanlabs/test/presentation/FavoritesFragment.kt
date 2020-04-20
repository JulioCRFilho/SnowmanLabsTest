package com.snowmanlabs.test.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider

import com.snowmanlabs.test.R
import com.snowmanlabs.test.utils.bottomBar
import com.snowmanlabs.test.viewModel.FavoritesViewModel

class FavoritesFragment : Fragment() {
    private lateinit var viewModel: FavoritesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FavoritesViewModel::class.java)
    }

    override fun onCreateView(i: LayoutInflater, v: ViewGroup?, s: Bundle?): View? {
        bottomBar(true)
        return i.inflate(R.layout.favorites_fragment, v, false)
    }
}
