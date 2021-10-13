package com.example.chinchopaapp.likes

import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.chinchopaapp.BaseFragment
import com.example.chinchopaapp.R
import com.example.chinchopaapp.databinding.FragmentNewsBinding

class BookmarksFragment: BaseFragment(R.layout.fragment_bookmarks){
    private val viewBinding by viewBinding(FragmentNewsBinding::bind)

    private val viewModel: BookmarksViewModel by viewModels()
}