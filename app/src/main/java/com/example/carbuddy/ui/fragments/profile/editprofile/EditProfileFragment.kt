package com.example.carbuddy.ui.fragments.profile.editprofile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.carbuddy.R
import com.example.carbuddy.databinding.FragmentEditProfileBinding
import com.example.carbuddy.utils.DateTimeUtils
import com.example.carbuddy.utils.Dialogs
import com.example.carbuddy.utils.ifEmpty
import com.example.carbuddy.utils.toast

class EditProfileFragment : Fragment() {
    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!
    private val ccp by lazy { binding.countryCodePicker }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        inIt()
        return binding.root
    }

    private fun inIt() {
        ccp.registerCarrierNumberEditText(binding.phoneNumber)
        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.backArrow.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.etDateOfBirth.setOnClickListener {
            Dialogs.showDatePickerDialog(requireContext()) {
                binding.etDateOfBirth.setText(DateTimeUtils.formatDateOfBirth(it))
            }
        }

        binding.btnUpdate.setOnClickListener {
            if (validateForm()) {

            }
        }
    }

    private fun validateForm(): Boolean {
        val fullNameEditText = binding.etFullName
        val dateOfBirthEditText = binding.etDateOfBirth
        val addressEditText = binding.etAddress
        val phoneNumberEditText = binding.phoneNumber

        fullNameEditText.error = null
        dateOfBirthEditText.error = null
        addressEditText.error = null

        return when {
            fullNameEditText.ifEmpty("Full Name is required") -> {
                false
            }

            dateOfBirthEditText.ifEmpty("Date of Birth is required") -> {
                false
            }

            phoneNumberEditText.ifEmpty("Phone number is required") -> {
                false
            }

            !ccp.isValidFullNumber -> {
                toast("Invalid phone number for ${ccp.selectedCountryName}")
                false
            }

            addressEditText.ifEmpty("Address is required") -> {
                false
            }

            else -> true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        ccp.deregisterCarrierNumberEditText()
        _binding = null
    }
}