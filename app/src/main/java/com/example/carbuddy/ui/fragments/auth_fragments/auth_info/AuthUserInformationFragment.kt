package com.example.carbuddy.ui.fragments.auth_fragments.auth_info

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.example.carbuddy.databinding.FragmentAuthUserInformationBinding
import com.example.carbuddy.utils.DateTimeUtils
import com.example.carbuddy.utils.Dialogs
import com.example.carbuddy.utils.ifEmailNotMatches
import com.example.carbuddy.utils.ifEmpty
import com.example.carbuddy.utils.toast
import com.github.dhaval2404.imagepicker.ImagePicker

class AuthUserInformationFragment : Fragment() {
    private lateinit var binding: FragmentAuthUserInformationBinding
    private val ccp by lazy { binding.countryCodePicker }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthUserInformationBinding.inflate(layoutInflater, null, false)
        inIt()
        return binding.root
    }

    private fun inIt() {
        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.backArrow.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnImagePicker.setOnClickListener {
            setUpImagePicker()
        }
        binding.etDateOfBirth.setOnClickListener {
            Dialogs.showDatePickerDialog(requireContext()) {
                binding.etDateOfBirth.setText(DateTimeUtils.formatDateOfBirth(it))
            }
        }
        binding.btnContinue.setOnClickListener {
            if (validateForm()) {
                toast("Loged in")
            }
        }
    }

    private val launcherForImagePicker =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val resultCode = result.resultCode
            val data = result.data
            when (resultCode) {
                Activity.RESULT_OK -> {
                    binding.profilePic.setImageURI(data?.data!!)
                }

                ImagePicker.RESULT_ERROR -> {
                    toast(ImagePicker.getError(data))
                }

                else -> {
                    toast("Task Cancelled")
                }
            }
        }

    private fun setUpImagePicker() {
        ImagePicker.with(requireActivity())
            .crop()
            .createIntent {
                launcherForImagePicker.launch(it)
            }
    }

    private fun validateForm(): Boolean {
        ccp.registerCarrierNumberEditText(binding.phoneNumber)

        val fullNameEditText = binding.etFullName
        val emailEditText = binding.etEmail
        val dateOfBirthEditText = binding.etDateOfBirth
        val addressEditText = binding.etAddress
        val phoneNumberEditText = binding.phoneNumber

        fullNameEditText.error = null
        emailEditText.error = null
        dateOfBirthEditText.error = null
        addressEditText.error = null

        return when {
            fullNameEditText.ifEmpty("Full Name is required") -> { false }
            emailEditText.ifEmpty("Email address is required") -> { false }
            emailEditText.ifEmailNotMatches("Invalid email format") -> { false }
            dateOfBirthEditText.ifEmpty("Date of Birth is required") -> { false }
            phoneNumberEditText.ifEmpty("Phone number is required")-> { false }
            !ccp.isValidFullNumber -> {
                toast("Invalid phone number for ${ccp.selectedCountryName}")
                false
            }
            addressEditText.ifEmpty("Address is required") -> { false }
            else -> true
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        ccp.deregisterCarrierNumberEditText()
        binding.unbind()
    }
}