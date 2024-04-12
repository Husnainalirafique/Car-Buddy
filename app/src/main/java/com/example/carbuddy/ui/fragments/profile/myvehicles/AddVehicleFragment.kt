package com.example.carbuddy.ui.fragments.profile.myvehicles

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.carbuddy.R
import com.example.carbuddy.data.models.vehicles.ModelVehicle
import com.example.carbuddy.databinding.FragmentAddVehicleBinding
import com.example.carbuddy.ui.fragments.profile.VmProfile
import com.example.carbuddy.utils.DataState
import com.example.carbuddy.utils.KeyboardUtils.hideKeyboard
import com.example.carbuddy.utils.ProgressDialogUtil.dismissProgressDialog
import com.example.carbuddy.utils.ProgressDialogUtil.showProgressDialog
import com.example.carbuddy.utils.gone
import com.example.carbuddy.utils.toast
import com.example.carbuddy.utils.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddVehicleFragment : Fragment() {
    private var _binding: FragmentAddVehicleBinding? = null
    private val binding get() = _binding!!
    private val vmProfile: VmProfile by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddVehicleBinding.inflate(inflater, container, false)
        inIt()
        return binding.root
    }

    private fun inIt() {
        setOnClickListener()
        setUpObserver()
    }

    private fun setOnClickListener() {
        binding.btnAddVehicle.setOnClickListener {
            if (isValid()) {
                hideKeyboard(it)
                addVehicle()
            }
        }
        binding.backArrow.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun addVehicle() {
        val make = binding.etMake.text.toString().trim()
        val model = binding.etModel.text.toString().trim()
        val year = binding.etYear.text.toString().toInt()
        val lpn = binding.etLpn.text.toString().trim()

        val vehicle = ModelVehicle(model, make, year, lpn)
        vmProfile.addVehicle(vehicle)
    }

    private fun setUpObserver() {
        vmProfile.addVehicleStatus.observe(viewLifecycleOwner) {
            when (it) {
                is DataState.Success -> {
                    findNavController().popBackStack()
                }

                is DataState.Error -> {
                    toast(it.errorMessage)
                    binding.tvAddVehicle.visible()
                    binding.progressBar.gone()
                }

                is DataState.Loading -> {
                    binding.progressBar.visible()
                    binding.tvAddVehicle.gone()
                }
            }
        }
    }

    private fun isValid(): Boolean {
        val make = binding.etMake
        val model = binding.etModel
        val year = binding.etYear
        val lpn = binding.etLpn

        make.error = null
        model.error = null
        year.error = null
        lpn.error = null

        return when {
            make.text.isNullOrEmpty() -> {
                make.error = "Please fill!"
                make.requestFocus()
                false
            }

            model.text.isNullOrEmpty() -> {
                model.error = "Please fill!"
                model.requestFocus()
                false
            }

            year.text.isNullOrEmpty() -> {
                year.error = "Please fill!"
                year.requestFocus()
                false
            }

            lpn.text.isNullOrEmpty() -> {
                lpn.error = "Please fill!"
                lpn.requestFocus()
                false
            }

            else -> {
                true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}