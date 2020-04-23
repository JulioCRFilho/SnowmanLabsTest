package com.snowmanlabs.test.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView

import com.snowmanlabs.test.R
import com.snowmanlabs.test.databinding.ProfileFragmentBinding
import com.snowmanlabs.test.interactor.ProfileInterface
import com.snowmanlabs.test.utils.InteractorDialog
import com.snowmanlabs.test.utils.bottomBar
import com.snowmanlabs.test.viewModel.ProfileViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.profile_fragment.*

class ProfileFragment : Fragment(), ProfileInterface.UI {
    private lateinit var viewModel: ProfileViewModel
    private lateinit var binding: ProfileFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        viewModel.interactor = this
    }

    override fun onCreateView(i: LayoutInflater, v: ViewGroup?, s: Bundle?): View? {
        bottomBar(true)
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.profile_fragment,
            v,
            false
        )
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val nav = activity?.parent?.findViewById<BottomNavigationView>(R.id.bottom_navigation)
        nav?.selectedItemId = R.id.icUser

        Picasso.get()
            .load(viewModel.user?.photoUrl)
            .resize(30, 30)
            .placeholder(R.drawable.user_ic)
            .error(R.drawable.noimage)
            .into(userPhoto)
    }

    override fun onLoading() {
        viewModel.status.value = Pair(1, null)
        InteractorDialog(requireContext(), owner = this, status = viewModel.status).show()
    }

    override fun onError(msg: String) {
        viewModel.status.value = Pair(0, msg)
    }

    override fun onSuccess(msg: String?) {
        viewModel.status.value = Pair(2, msg)
    }
}
