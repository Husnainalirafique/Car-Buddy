package com.example.carbuddy.ui.fragments.auth.signup

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.carbuddy.R
import com.example.carbuddy.databinding.FragmentSignUpBinding
import com.example.carbuddy.utils.Constants
import com.example.carbuddy.utils.Spanny
import com.example.carbuddy.utils.getColorFromId
import com.example.carbuddy.utils.setEditTextFocusChangeBackground
import com.example.carbuddy.utils.setPasswordVisibilityToggle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
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
    }

    private fun setOnClickListener() {
        binding.backArrow.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnSignup.setOnClickListener {
            if (isValid()) {
                val bundle = Bundle()
                bundle.apply {
                    putString(Constants.KEY_EMAIL, binding.etEmail.text.toString())
                    putString(Constants.KEY_PASSWORD, binding.etPassword.text.toString())
                }
                findNavController().navigate(
                    R.id.action_signUpFragment_to_fillProfileFragment,
                    bundle
                )
            }
        }
    }

    private fun setUpUi() {
        setPasswordVisibilityToggle(binding.etPassword)
        setEditTextFocusChangeBackground(binding.etEmail, binding.etPassword)
        Spanny.spannableText(
            binding.textSignup, "Already have an account? Sign in",
            "Sign in", getColorFromId(R.color.color_primary)
        ) {
            /*........ On click of sign in ........*/
            findNavController().navigate(R.id.logInFragment)
        }
    }

    private fun isValid(): Boolean {
        binding.etEmail.error = null
        binding.etPassword.error = null
        return when {
            binding.etEmail.text.isNullOrEmpty() -> {
                binding.etEmail.error = "Please fill!"
                binding.etEmail.requestFocus()
                false
            }

            binding.etPassword.text.isNullOrEmpty() -> {
                binding.etPassword.error = "Please fill!"
                binding.etPassword.requestFocus()
                false
            }

            binding.etPassword.text.toString().length < 7 -> {
                binding.etPassword.error = "Password must be at least 7 characters long"
                binding.etPassword.requestFocus()
                false
            }

            !Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.text.toString()).matches() -> {
                binding.etEmail.error = "Invalid email pattern"
                binding.etEmail.requestFocus()
                false
            }

            else -> {
                true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
    }
}