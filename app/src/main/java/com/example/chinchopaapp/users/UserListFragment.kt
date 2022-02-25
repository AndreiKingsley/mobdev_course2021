package com.example.chinchopaapp.users

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.chinchopaapp.BaseFragment
import com.example.chinchopaapp.MainActivity
import com.example.chinchopaapp.R
import com.example.chinchopaapp.databinding.FragmentUserlistBinding
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect
import timber.log.Timber

@AndroidEntryPoint
class UserListFragment : BaseFragment(R.layout.fragment_userlist) {
    private val viewBinding by viewBinding(FragmentUserlistBinding::bind)

    private val viewModel: UserListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.searchUsers()

        setupRecyclerView()
        subscribeToViewState()

        viewBinding.usersToolbar.applyInsetter {
            type(statusBars = true) { margin() }
        }

        viewBinding.searchViewUsers.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    viewModel.searchUsers(
                        p0,
                        viewBinding.bySubstringCheckBox.isChecked
                    )
                    return false
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    return false
                }

            }
        )
    }

    private fun subscribeToViewState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState.collect { viewState -> renderViewState(viewState) }
            }
        }
    }

    private fun setupRecyclerView(): UserAdapter = UserAdapter().also {
        viewBinding.userListRecyclerView.layoutManager = LinearLayoutManager(activity)
        viewBinding.userListRecyclerView.adapter = it
    }

    private fun renderViewState(viewState: UserListViewModel.ViewState) {
        when (viewState) {
            is UserListViewModel.ViewState.Loading -> {
                viewBinding.noUsersFoundStatusField.isVisible = false
                viewBinding.userListRecyclerView.isVisible = false
                viewBinding.progressBar.isVisible = true
            }
            is UserListViewModel.ViewState.Data -> {
                viewBinding.userListRecyclerView.isVisible = true
                (viewBinding.userListRecyclerView.adapter as UserAdapter).apply {
                    userList = viewState.userList
                    viewBinding.noUsersFoundStatusField.isVisible = userList.isEmpty()
                    notifyDataSetChanged()
                }
                viewBinding.progressBar.isVisible = false
            }
        }
    }
}