package com.example.chinchopaapp.profile

import androidx.fragment.app.viewModels
import com.example.chinchopaapp.BaseFragment
import com.example.chinchopaapp.R
import com.example.myapplication.ui.profile.ProfileViewModel
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.chinchopaapp.databinding.FragmentProfileBinding

class ProfileFragment : BaseFragment(R.layout.fragment_profile) {
    val viewModel: ProfileViewModel by viewModels()
    private val viewBinding by viewBinding(FragmentProfileBinding::bind)
}