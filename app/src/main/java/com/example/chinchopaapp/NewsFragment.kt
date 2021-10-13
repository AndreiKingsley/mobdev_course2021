package com.example.chinchopaapp

import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.chinchopaapp.databinding.FragmentNewsBinding

class NewsFragment: BaseFragment(R.layout.fragment_news){
    private val viewBinding by viewBinding(FragmentNewsBinding::bind)

    private val viewModel: NewsViewModel by viewModels()
}