package com.example.carbuddy.ui.fragments.profile.editprofile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.carbuddy.R
import com.example.carbuddy.data.models.ModelUser
import com.example.carbuddy.databinding.FragmentEditProfileBinding
import com.example.carbuddy.preferences.PreferenceManager
import com.example.carbuddy.ui.fragments.profile.VmProfile
import com.example.carbuddy.utils.DataState
import com.example.carbuddy.utils.DateTimeUtils
import com.example.carbuddy.utils.Dialogs
import com.example.carbuddy.utils.ProgressDialogUtil.dismissProgressDialog
import com.example.carbuddy.utils.ProgressDialogUtil.showProgressDialog
import com.example.carbuddy.utils.ifEmpty
import com.example.carbuddy.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EditProfileFragment : Fragment() {
    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!
    private val ccp by lazy { binding.countryCodePicker }

    //Pref
    @Inject
    lateinit var preferenceManager: PreferenceManager

    //Views
    private lateinit var etFullName: EditText
    private lateinit var etDob: EditText
    private lateinit var etPhoneNum: EditText
    private lateinit var etAddress: EditText
    private lateinit var profileImgUri: String
    private lateinit var userEmail: String
    private lateinit var userPassword: String

    //view model
    private val vmProfile: VmProfile by viewModels()


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
        inItViews()
        getUserFromPrefAndSet()
        setOnClickListener()
        setUpObserver()
    }

    private fun inItViews() {
        ccp.registerCarrierNumberEditText(binding.phoneNumber)
        etFullName = binding.etFullName
        etDob = binding.etDateOfBirth
        etPhoneNum = binding.phoneNumber
        etAddress = binding.etAddress
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
                updateUser()
            }
        }
    }

    private fun updateUser() {
        val fullName = etFullName.text.toString().trim()
        val dateOfBirth = etDob.text.toString().trim()
        val phoneNumber = etPhoneNum.text.toString().trim()
        val address = etAddress.text.toString().trim()
        val user = ModelUser(
            userEmail,
            userPassword,
            fullName,
            dateOfBirth,
            phoneNumber,
            address,
            profileImgUri
        )
        vmProfile.updateUser(user)
    }

    private fun getUserFromPrefAndSet() {
        val user = preferenceManager.getUserData()
        user?.let {
            etFullName.setText(it.fullName)
            etDob.setText(it.dob)
            etPhoneNum.setText(it.phoneNumber)
            etAddress.setText(it.address)
            this.profileImgUri = it.profileImageUri
            this.userEmail = it.email
            this.userPassword = it.password
        }
    }

    private fun setUpObserver() {
        vmProfile.updateStatus.observe(viewLifecycleOwner) {
            when (it) {
                is DataState.Success -> {
                    toast("Updated")
                    findNavController().popBackStack()
                    dismissProgressDialog()
                }

                is DataState.Error -> {
                    toast(it.errorMessage)
                    dismissProgressDialog()
                }

                is DataState.Loading -> {
                    showProgressDialog()
                }
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