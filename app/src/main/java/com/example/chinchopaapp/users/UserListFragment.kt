package com.example.chinchopaapp.users

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.chinchopaapp.BaseFragment
import com.example.chinchopaapp.R
import com.example.chinchopaapp.databinding.FragmentUserlistBinding
import dev.chrisbanes.insetter.applyInsetter

class UserListFragment: BaseFragment(R.layout.fragment_userlist){
    private val viewBinding by viewBinding(FragmentUserlistBinding::bind)

    private val viewModel: UserListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.usersToolbar.applyInsetter {
            type(statusBars = true) { margin() }
        }
    }
}