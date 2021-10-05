package com.example.chinchopaapp

import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.chinchopaapp.databinding.FragmentSignInBinding

class SignInFragment: BaseFragment(R.layout.fragment_sign_in) {
    val viewModel: SignInViewModel by viewModels()
    private val viewBinding by viewBinding(FragmentSignInBinding::bind)
}
