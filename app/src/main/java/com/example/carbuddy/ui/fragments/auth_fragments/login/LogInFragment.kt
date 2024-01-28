package com.example.carbuddy.ui.fragments.auth_fragments.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.carbuddy.R
import com.example.carbuddy.databinding.FragmentLogInBinding
import com.example.carbuddy.utils.BackPressedExtensions.goBackPressed
import com.example.carbuddy.utils.Spanny
import com.example.carbuddy.utils.getColorFromId
import com.example.carbuddy.utils.setEditTextFocusChangeBackground
import com.example.carbuddy.utils.setPasswordVisibilityToggle
import com.example.carbuddy.utils.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LogInFragment :Fragment() {
    private lateinit var binding: FragmentLogInBinding
    private val vm:LogInVm by viewModels()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLogInBinding.inflate(layoutInflater, null, false)
        inIt()
        return binding.root
    }

    private fun inIt() {
        setUpUI()
        setOnClickListeners()
        backPressed()
    }

    private fun setOnClickListeners() {
        binding.btnGoogleLogIn.setOnClickListener {
            findNavController().navigate(R.id.action_logInFragment_to_signUpFragment)
        }
        binding.backArrow.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnSignIn.setOnClickListener {
            if (validateForm()) toast("Signed in")
        }
    }

    private fun setUpUI() {
        setEditTextFocusChangeBackground(binding.signInEmailEditText,binding.signInPasswordEditText)
        setPasswordVisibilityToggle(binding.signInPasswordEditText)
        Spanny.spannableText(binding.textSignIn,"Don't have an account? Sign Up","Sign Up",getColorFromId(R.color.color_primary)){
            findNavController().navigate(R.id.action_logInFragment_to_signUpFragment)
        }
    }

    private fun validateForm():Boolean{
        if (binding.signInEmailEditText.text!!.isEmpty()){
            binding.signInEmailEditText.error = resources.getString(R.string.string_no_email_error)
            return false
        }
        if (binding.signInPasswordEditText.text!!.isEmpty()){
            binding.signInPasswordEditText.error = resources.getString(R.string.string_no_password_error)
            return false
        }
        return true
    }

    private fun backPressed() {
        goBackPressed {
            requireActivity().finishAffinity()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
    }
}