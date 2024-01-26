package com.example.carbuddy.ui.fragments.auth_fragments.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.carbuddy.R
import com.example.carbuddy.databinding.FragmentSignUpBinding
import com.example.carbuddy.utils.Spanny
import com.example.carbuddy.utils.setEditTextFocusChangeBackground
import com.example.carbuddy.utils.setPasswordVisibilityToggle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment :Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSignUpBinding.inflate(layoutInflater, null, false)
        inIt()
        return binding.root
    }

    private fun inIt() {
        setOnClickListener()
        handlingEditTextsAndTextViews()
    }

    private fun setOnClickListener() {
        binding.backArrow.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_authUserInformationFragment)
        }
    }

    private fun handlingEditTextsAndTextViews(){
        setPasswordVisibilityToggle(binding.signUpPasswordEditText)
        setEditTextFocusChangeBackground(binding.signUpEmailEditText,binding.signUpPasswordEditText)
        Spanny.spannableText(binding.textSignup,"Already have an account? Sign in","Sign in",resources.getColor(R.color.color_primary,null)){
            findNavController().navigate(R.id.logInFragment)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
    }
}