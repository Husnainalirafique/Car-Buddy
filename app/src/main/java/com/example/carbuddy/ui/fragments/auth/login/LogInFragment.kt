package com.example.carbuddy.ui.fragments.auth.login

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.carbuddy.R
import com.example.carbuddy.databinding.FragmentLogInBinding
import com.example.carbuddy.ui.activities.AfterAuthActivity
import com.example.carbuddy.ui.fragments.auth.VmAuth
import com.example.carbuddy.utils.BackPressedExtensions.goBackPressed
import com.example.carbuddy.utils.DataState
import com.example.carbuddy.utils.ProgressDialogUtil.dismissProgressDialog
import com.example.carbuddy.utils.ProgressDialogUtil.showProgressDialog
import com.example.carbuddy.utils.Spanny
import com.example.carbuddy.utils.getColorFromId
import com.example.carbuddy.utils.setEditTextFocusChangeBackground
import com.example.carbuddy.utils.setPasswordVisibilityToggle
import com.example.carbuddy.utils.startActivity
import com.example.carbuddy.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LogInFragment : Fragment() {
    private lateinit var binding: FragmentLogInBinding
    private val vm: VmAuth by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLogInBinding.inflate(layoutInflater, null, false)
        inIt()
        return binding.root
    }

    private fun inIt() {
        setUpUI()
        setOnClickListeners()
        backPressed()
        setUpObserver()
    }

    private fun setOnClickListeners() {
        binding.backArrow.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnSignIn.setOnClickListener {
            if (isValid()) {
                login()
            }
        }
    }

    private fun login() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        lifecycleScope.launch {
            vm.loginWithEmailPass(email, password)
        }
    }

    private fun setUpObserver() {
        vm.loginStatus.observe(viewLifecycleOwner) { dataState ->
            when (dataState) {
                is DataState.Success -> {
                    toast("Loged In successfully")
                    dismissProgressDialog()
                    startActivity(AfterAuthActivity::class.java)
                    requireActivity().finish()
                }

                is DataState.Error -> {
                    toast(dataState.errorMessage)
                    dismissProgressDialog()
                }

                is DataState.Loading -> {
                    showProgressDialog()
                }
            }
        }
    }

    private fun setUpUI() {
        setEditTextFocusChangeBackground(binding.etEmail, binding.etPassword)
        setPasswordVisibilityToggle(binding.etPassword)
        Spanny.spannableText(
            binding.textSignIn,
            "Don't have an account? Sign Up",
            "Sign Up",
            getColorFromId(R.color.color_primary)
        ) {
            findNavController().navigate(R.id.action_logInFragment_to_signUpFragment)
        }
    }

    private fun isValid(): Boolean {
        val email = binding.etEmail
        val password = binding.etEmail

        email.error = null
        password.error = null
        return when {
            email.text.isNullOrEmpty() -> {
                email.error = "Please fill!"
                email.requestFocus()
                false
            }

            password.text.isNullOrEmpty() -> {
                password.error = "Please fill!"
                password.requestFocus()
                false
            }

            password.text.toString().length < 7 -> {
                password.error = "Password must be at least 7 characters long"
                password.requestFocus()
                false
            }

            !Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches() -> {
                email.error = "Invalid email pattern"
                email.requestFocus()
                false
            }

            else -> {
                true
            }
        }
    }

    private fun backPressed() = goBackPressed { requireActivity().finishAffinity() }


    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
    }
}