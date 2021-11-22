package com.example.chinchopaapp

import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.chinchopaapp.databinding.FragmentUserlistBinding

class UserListFragment: BaseFragment(R.layout.fragment_userlist){
    private val viewBinding by viewBinding(FragmentUserlistBinding::bind)

    private val viewModel: UserListViewModel by viewModels()
}