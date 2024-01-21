package com.example.carbuddy.ui.fragments.auth_fragments.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.carbuddy.R
import com.example.carbuddy.databinding.FragmentLogInBinding
import com.example.carbuddy.utils.Spanny
import com.example.carbuddy.utils.setEditTextFocusChangeBackground
import com.example.carbuddy.utils.setPasswordVisibilityToggle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LogInFragment :Fragment() {
    private lateinit var binding: FragmentLogInBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLogInBinding.inflate(layoutInflater, null, false)
        inIt()
        return binding.root
    }

    private fun inIt() {
        setUpUI()
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        binding.btnGoogleLogIn.setOnClickListener {
            findNavController().navigate(R.id.action_logInFragment_to_signUpFragment)
        }
        binding.backArrow.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setUpUI() {
        setEditTextFocusChangeBackground(binding.signInEmailEditText,binding.signInPasswordEditText)
        setPasswordVisibilityToggle(binding.signInPasswordEditText)
        Spanny.spannableText(binding.textSignIn,"Don't have an account? Sign Up","Sign Up",resources.getColor(R.color.color_primary,null)){
            findNavController().navigate(R.id.action_logInFragment_to_signUpFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
    }
}