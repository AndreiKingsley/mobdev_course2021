package com.example.chinchopaapp.email_confirmation

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.chinchopaapp.BaseFragment
import com.example.chinchopaapp.R
import com.example.chinchopaapp.databinding.FragmentEmailConfirmationBinding
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter

@AndroidEntryPoint
class EmailConfirmationFragment : BaseFragment(R.layout.fragment_email_confirmation) {

    private val viewBinding by viewBinding(FragmentEmailConfirmationBinding::bind)

    private val viewModel: EmailConfirmationViewModel by viewModels()

    private fun iniTimer() = object: CountDownTimer(10*1000, 1000){
        override fun onTick(millisUntilFinished: Long) {
            viewBinding.emailVerificationSendCodeTimer.text =
                resources.getString(R.string.email_verification_send_code_timer_string)
                    .replace("%s", (millisUntilFinished/1000).toString())
        }

        override fun onFinish() {
            viewBinding.sendCodeButton.isEnabled = true
        }
    }

    var timer: CountDownTimer = iniTimer()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.backButton.applyInsetter {
            type(statusBars = true) { margin() }
        }
        viewBinding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        viewBinding.VerificationCodeEdit.viewBinding.realVerificationCodeEditText.addTextChangedListener {
            viewBinding.confirmCodeButton.isEnabled = viewBinding.VerificationCodeEdit.allIsFilled
        }

        viewBinding.sendCodeButton.setOnClickListener {
            // todo send
            setupResend()
        }
        setupResend()

    }

    private fun setupResend(){
        viewBinding.sendCodeButton.isEnabled = false
        timer.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }

}
