package com.example.carbuddy.ui.fragments.auth_fragments.auth_info

import android.app.Activity
import android.app.DatePickerDialog
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
import com.example.carbuddy.utils.toast
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.Calendar

class AuthUserInformationFragment : Fragment() {
    private lateinit var binding: FragmentAuthUserInformationBinding
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
            Dialogs.showDatePickerDialog(requireContext()){
                binding.etDateOfBirth.setText(DateTimeUtils.formatDateOfBirth(it))
            }
        }
    }

    private val launcherForImagePicker = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
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

    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
    }
}