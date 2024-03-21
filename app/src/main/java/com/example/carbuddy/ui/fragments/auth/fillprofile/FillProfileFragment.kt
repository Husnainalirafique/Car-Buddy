package com.example.carbuddy.ui.fragments.auth.fillprofile

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.carbuddy.data.models.ModelUser
import com.example.carbuddy.databinding.FragmentFillProfileBinding
import com.example.carbuddy.ui.activities.AfterAuthActivity
import com.example.carbuddy.ui.fragments.auth.VmAuth
import com.example.carbuddy.utils.Constants
import com.example.carbuddy.utils.DataState
import com.example.carbuddy.utils.DateTimeUtils
import com.example.carbuddy.utils.Dialogs
import com.example.carbuddy.utils.ProgressDialogUtil.dismissProgressDialog
import com.example.carbuddy.utils.ProgressDialogUtil.showProgressDialog
import com.example.carbuddy.utils.ifEmpty
import com.example.carbuddy.utils.startActivity
import com.example.carbuddy.utils.toast
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FillProfileFragment : Fragment() {
    private lateinit var binding: FragmentFillProfileBinding
    private val ccp by lazy { binding.countryCodePicker }
    private val vm: VmAuth by viewModels()
    private lateinit var profileImgUri: Uri
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFillProfileBinding.inflate(layoutInflater, null, false)
        inIt()
        return binding.root
    }

    private fun inIt() {
        ccp.registerCarrierNumberEditText(binding.phoneNumber)
        setOnClickListener()
        setupObserver()
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
                createAccount()
            }
        }
    }

    private fun createAccount() {
        val email = arguments?.getString(Constants.KEY_EMAIL)!!
        val password = arguments?.getString(Constants.KEY_PASSWORD)!!
        val fullName = binding.etFullName.text.toString()
        val dateOfBirth = binding.etDateOfBirth.text.toString()
        val phoneNumber = ccp.fullNumberWithPlus
        val address = binding.etAddress.text.toString()

        val user = ModelUser(
            email,
            password,
            fullName,
            dateOfBirth,
            phoneNumber,
            address,
            profileImgUri.toString()
        )

        lifecycleScope.launch(Dispatchers.IO) {
            vm.signUpWithEmailPass(user)
        }
    }

    private fun setupObserver() {
        vm.signUpStatus.observe(viewLifecycleOwner) { dataState ->
            when (dataState) {
                is DataState.Success -> {
                    toast("Account created successfully")
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

    private var isImageChanged = false
    private val launcherForImagePicker =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val resultCode = result.resultCode
            val data = result.data
            when (resultCode) {
                Activity.RESULT_OK -> {
                    val uri = data?.data
                    if (uri != null) {
                        profileImgUri = uri
                        binding.profilePic.setImageURI(data.data)
                        isImageChanged = true
                    }
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
        val fullNameEditText = binding.etFullName
        val dateOfBirthEditText = binding.etDateOfBirth
        val addressEditText = binding.etAddress
        val phoneNumberEditText = binding.phoneNumber

        fullNameEditText.error = null
        dateOfBirthEditText.error = null
        addressEditText.error = null

        return when {
            fullNameEditText.ifEmpty("Full Name is required") -> { false }
            dateOfBirthEditText.ifEmpty("Date of Birth is required") -> { false }
            phoneNumberEditText.ifEmpty("Phone number is required")-> { false }

            !ccp.isValidFullNumber -> {
                toast("Invalid phone number for ${ccp.selectedCountryName}")
                false
            }

            !isImageChanged -> {
                toast("Please Select an Image!")
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