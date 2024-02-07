package com.example.carbuddy.ui.fragments.auth_fragments.login

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.carbuddy.R
import com.example.carbuddy.data.Email
import com.example.carbuddy.data.Password
import com.example.carbuddy.databinding.FragmentLogInBinding
import com.example.carbuddy.utils.BackPressedExtensions.goBackPressed
import com.example.carbuddy.utils.Spanny
import com.example.carbuddy.utils.getColorFromId
import com.example.carbuddy.utils.setEditTextFocusChangeBackground
import com.example.carbuddy.utils.setPasswordVisibilityToggle
import com.example.carbuddy.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.log

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
        validateAndLogin()
    }

    private fun setOnClickListeners() {
        binding.btnGoogleLogIn.setOnClickListener {
            findNavController().navigate(R.id.action_logInFragment_to_signUpFragment)
        }
        binding.backArrow.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnSignIn.setOnClickListener {
            val email = binding.signInEmailEditText.text.toString()
            val password = binding.signInPasswordEditText.text.toString()
            vm.validateInput(Email(email), Password(password))
        }
    }

    private fun setUpUI() {
        setEditTextFocusChangeBackground(binding.signInEmailEditText,binding.signInPasswordEditText)
        setPasswordVisibilityToggle(binding.signInPasswordEditText)
        Spanny.spannableText(binding.textSignIn,"Don't have an account? Sign Up","Sign Up",getColorFromId(R.color.color_primary)){
            findNavController().navigate(R.id.action_logInFragment_to_signUpFragment)
        }
    }

    private fun validateAndLogin(){
        vm.validationResult.observe(requireActivity()){ (emailError,passwordError) ->
            binding.signInEmailEditText.error = emailError.value
            binding.signInPasswordEditText.error = passwordError.value
            if (emailError.value == null && passwordError.value == null){
                login()
            }
        }
    }

    private fun login() {
        toast("Loged in")
    }


    private fun backPressed() {
        goBackPressed {
            requireActivity().finishAffinity()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        vm.validationResult.removeObservers(requireActivity())
        binding.unbind()
    }
}