package com.example.chinchopaapp

import com.example.chinchopaapp.databinding.ItemOnboardingTextBinding
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding

fun onboardingTextAdapterDelegate() =
    adapterDelegateViewBinding<String, CharSequence, ItemOnboardingTextBinding>(
        viewBinding = { layoutInflater, parent ->
            ItemOnboardingTextBinding.inflate(layoutInflater, parent, false)
        },
        block = {
            bind {
                binding.textView.text = item
            }
        }
    )
