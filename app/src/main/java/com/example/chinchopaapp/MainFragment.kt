package com.example.chinchopaapp

import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.chinchopaapp.databinding.FragmentMainBinding

class MainFragment: BaseFragment(R.layout.fragment_main) {
    // todo val viewModel: MAinViewModel by viewModels()
    private val viewBinding by viewBinding(FragmentMainBinding::bind)
}