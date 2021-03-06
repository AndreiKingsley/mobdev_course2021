package com.example.chinchopaapp.profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.chinchopaapp.BaseFragment
import com.example.chinchopaapp.R
import com.example.chinchopaapp.databinding.FragmentProfileBinding
import com.example.chinchopaapp.users.UserAdapter
import com.example.chinchopaapp.users.UserListViewModel
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ProfileFragment : BaseFragment(R.layout.fragment_profile) {

    private val viewBinding by viewBinding(FragmentProfileBinding::bind)

    private val viewModel: ProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToEvents()
        viewModel.loadProfile()
        subscribeToViewState()
        viewBinding.logoutButton.applyInsetter {
            type(statusBars = true) { margin() }
        }
        viewBinding.profileToolbar.applyInsetter {
            type(statusBars = true) { margin() }
        }
        viewBinding.logoutButton.setOnClickListener {
            viewModel.logout()
        }
    }

    private fun subscribeToViewState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState.collect { viewState -> renderViewState(viewState) }
            }
        }
    }

    private fun renderViewState(viewState: ProfileViewModel.ViewState) {
        when (viewState) {
            is ProfileViewModel.ViewState.Loading -> {
                viewBinding.profileImage.isVisible = false
                viewBinding.nameTextView.isVisible = false
                viewBinding.infoTextView.isVisible = false

                viewBinding.progressBar.isVisible = true
            }
            is ProfileViewModel.ViewState.Data -> {

                viewBinding.profileImage.isVisible = true
                viewBinding.nameTextView.isVisible = true
                viewBinding.infoTextView.isVisible = true

                val profile = viewState.profile
                // TODO
                Glide.with(viewBinding.profileImage)
                    .load(profile.avatarUrl)
                    .into(viewBinding.profileImage)
                viewBinding.nameTextView.text = "${profile.firstName} ${profile.lastName}"
                viewBinding.infoTextView.text =
                    """
                        Username: ${profile.userName}
                        Age: ${profile.age}
                        Group: ${profile.groupName}
                    """.trimIndent()
                viewBinding.progressBar.isVisible = false
            }
        }
    }

    private fun subscribeToEvents() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.eventsFlow().collect { event ->
                    when (event) {
                        is ProfileViewModel.Event.LogoutError -> {
                            Toast
                                .makeText(
                                    requireContext(),
                                    R.string.common_general_error_text,
                                    Toast.LENGTH_LONG
                                )
                                .show()
                        }
                    }
                }
            }
        }
    }
}
