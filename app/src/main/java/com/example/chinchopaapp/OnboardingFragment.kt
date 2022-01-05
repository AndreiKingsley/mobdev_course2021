package com.example.chinchopaapp

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.chinchopaapp.databinding.FragmentOnboardingBinding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import timber.log.Timber


class OnboardingFragment : Fragment(R.layout.fragment_onboarding) {

    private var player: ExoPlayer? = null
    private var volumeUp = true
    private val viewBinding by viewBinding(FragmentOnboardingBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("ONBOARDING!!!!!!")
        setPlayer()
    }

    class AutoScroller(
        val viewPager: ViewPager2,
        val scope: CoroutineScope,
        val period: Long = 4000L
    ) {

        val mutex = Mutex()
        private var userLastInteract = System.currentTimeMillis()

        private fun nextState() {
            viewPager.currentItem = (viewPager.currentItem + 1) % 3 // TODO better
        }

        init {

            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageScrollStateChanged(state: Int) {
                    scope.launch {
                        mutex.withLock {
                            userLastInteract = System.currentTimeMillis()
                        }
                    }
                }

                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    scope.launch {
                        mutex.withLock {
                            userLastInteract = System.currentTimeMillis()
                        }
                    }
                }

                override fun onPageSelected(position: Int) {
                    scope.launch {
                        mutex.withLock {
                            userLastInteract = System.currentTimeMillis()
                        }
                    }
                }
            })


        }

        fun activateTimer() {
            with(scope) {
                launch {
                    while (true) {
                        val oldLastUserInteract =  mutex.withLock {
                            userLastInteract
                        }
                        val currentTime = System.currentTimeMillis()
                        val autoScrollingDelay = period - (currentTime - oldLastUserInteract)
                        delay(autoScrollingDelay)
                        val newLastUserInteract =  mutex.withLock {
                            userLastInteract
                        }
                        if (newLastUserInteract > oldLastUserInteract){
                            continue
                        }
                        nextState()
                    }
                }

            }
        }


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.playerView.player = player
        viewBinding.viewPager.setTextPages()
        viewBinding.viewPager.attachDots(viewBinding.onboardingTextTabLayout)
        /*
        viewBinding.signInButton.setOnClickListener {
            // TODO: Go to SignInFragment.
            Toast.makeText(requireContext(), "Нажата кнопка войти", Toast.LENGTH_SHORT).show()
        }


        viewBinding.signUpButton.setOnClickListener {
            // TODO: Go to SignUpFragment.
            Toast.makeText(requireContext(), "Нажата кнопка зарегистрироваться", Toast.LENGTH_SHORT)
                .show()
        } */
        viewBinding.viewPager.setOffscreenPageLimit(3)
        viewBinding.viewPager.setPageTransformer { page, position ->
            val myOffset: Float = position * -150
            if (position < -1) {
                page.setTranslationX(-myOffset)
            } else {
                page.setTranslationX(myOffset)
            }
        }

        viewBinding.signInButton.setOnClickListener {
            // TODO: Go to SignInFragment.
        //    Toast.makeText(requireContext(), "Нажата кнопка войти", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_onBoardingFragment_to_signInFragment)
        }

        viewBinding.signUpButton.setOnClickListener {
            // TODO: Go to SignInFragment.
            //    Toast.makeText(requireContext(), "Нажата кнопка войти", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_onBoardingFragment_to_signUpFragment)
        }

        val volumeControlButton = viewBinding.volumeControlButton
        viewBinding.volumeControlButton.setOnClickListener {
            if (volumeUp) {
                volumeUp = false
                player?.volume = 0.0F
                volumeControlButton.setImageResource(R.drawable.ic_volume_off_white_24dp)
            } else {
                volumeUp = true
                player?.volume = 1.0F
                volumeControlButton.setImageResource(R.drawable.ic_volume_up_white_24dp)
            }
        }
        lifecycleScope.launch {
            AutoScroller(viewBinding.viewPager, this).activateTimer()
        }
    }

    override fun onResume() {
        super.onResume()
        player?.play()
    }

    override fun onPause() {
        super.onPause()
        player?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.release()
    }

    private fun setPlayer() {
        player = SimpleExoPlayer.Builder(requireContext()).build().apply {
            addMediaItem(MediaItem.fromUri("asset:///onboarding.mp4"))
            repeatMode = Player.REPEAT_MODE_ALL
            volume = 0.0F
            prepare()
        }
        volumeUp = false
    }


    private fun ViewPager2.setTextPages() {
        adapter =
            ListDelegationAdapter(onboardingTextAdapterDelegate()).apply {
                items =
                    listOf(
                        getString(R.string.onboarding_view_pager_text_1),
                        getString(R.string.onboarding_view_pager_text_2),
                        getString(R.string.onboarding_view_pager_text_3)
                    )
            }
    }

    private fun ViewPager2.attachDots(tabLayout: TabLayout) {
        TabLayoutMediator(tabLayout, this) { _, _ -> }.attach()
    }
}