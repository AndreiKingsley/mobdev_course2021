package com.example.chinchopaapp

import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.chinchopaapp.databinding.FragmentEmailConfitmationBinding

class EmailConfirmationFragment : BaseFragment(R.layout.fragment_email_confitmation) {
    val viewModel: EmailConfirmationViewModel by viewModels()
    private val viewBinding by viewBinding(FragmentEmailConfitmationBinding::bind)
}
