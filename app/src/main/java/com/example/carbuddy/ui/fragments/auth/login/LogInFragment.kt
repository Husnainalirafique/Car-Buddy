package com.example.carbuddy.ui.fragments.auth.login

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LogInFragment : Fragment() {
    private lateinit var binding: FragmentLogInBinding
    private val vm: VmAuth by viewModels()
    private val signInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            vm.handleGoogleSignInResult(result.data)
        }

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
        binding.btnLoginWithGoogle.setOnClickListener {
            findNavController().navigate(R.id.action_logInFragment_to_signUpFragment)
        }

        binding.backArrow.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnSignIn.setOnClickListener {
            if (isValid()) {
                lifecycleScope.launch(Dispatchers.IO) {
                    vm.loginWithEmailPass(
                        binding.etEmail.text.toString(),
                        binding.etPassword.text.toString()
                    )
                }
            }
        }

        binding.btnLoginWithGoogle.setOnClickListener {
            vm.signInWithGoogle {
                signInLauncher.launch(it)
            }
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

    private fun backPressed() = goBackPressed { requireActivity().finishAffinity() }


    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
    }
}