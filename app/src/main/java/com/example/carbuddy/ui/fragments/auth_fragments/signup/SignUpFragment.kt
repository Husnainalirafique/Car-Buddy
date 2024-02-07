package com.example.carbuddy.ui.fragments.auth_fragments.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.carbuddy.R
import com.example.carbuddy.data.Email
import com.example.carbuddy.data.Password
import com.example.carbuddy.databinding.FragmentSignUpBinding
import com.example.carbuddy.ui.fragments.auth_fragments.login.LogInVm
import com.example.carbuddy.utils.Spanny
import com.example.carbuddy.utils.getColorFromId
import com.example.carbuddy.utils.setEditTextFocusChangeBackground
import com.example.carbuddy.utils.setPasswordVisibilityToggle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    private val validationVm: LogInVm by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(layoutInflater, null, false)
        inIt()
        return binding.root
    }

    private fun inIt() {
        setOnClickListener()
        setUpUi()
        validateAndSignUp()
    }

    private fun setOnClickListener() {
        binding.backArrow.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnSignUp.setOnClickListener {
            val email = binding.signUpEmailEditText.text.toString()
            val password = binding.signUpPasswordEditText.text.toString()
            validationVm.validateInput(Email(email), Password(password))
        }
    }

    private fun validateAndSignUp() {
        validationVm.validationResult.observe(requireActivity()) { (emailError, passwordError) ->
            binding.signUpEmailEditText.error = emailError.value
            binding.signUpPasswordEditText.error = passwordError.value
            if (emailError.value == null && passwordError.value == null) {
                findNavController().navigate(R.id.action_signUpFragment_to_authUserInformationFragment)
            }
        }
    }

    private fun setUpUi() {
        setPasswordVisibilityToggle(binding.signUpPasswordEditText)
        setEditTextFocusChangeBackground(binding.signUpEmailEditText, binding.signUpPasswordEditText)
        Spanny.spannableText(
            binding.textSignup,
            "Already have an account? Sign in",
            "Sign in",
            getColorFromId(R.color.color_primary)
        ) {
            /*........ On click of sign in ........*/
            findNavController().navigate(R.id.logInFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        validationVm.validationResult.removeObservers(requireActivity())
        binding.unbind()
    }
}