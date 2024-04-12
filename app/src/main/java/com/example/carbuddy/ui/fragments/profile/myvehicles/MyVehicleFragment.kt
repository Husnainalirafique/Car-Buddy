package com.example.carbuddy.ui.fragments.profile.myvehicles

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.carbuddy.R
import com.example.carbuddy.databinding.FragmentMyVehicleBinding
import com.example.carbuddy.ui.fragments.profile.VmProfile
import com.example.carbuddy.utils.DataState
import com.example.carbuddy.utils.ProgressDialogUtil.dismissProgressDialog
import com.example.carbuddy.utils.ProgressDialogUtil.showProgressDialog
import com.example.carbuddy.utils.gone
import com.example.carbuddy.utils.toast
import com.example.carbuddy.utils.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyVehicleFragment : Fragment() {
    private var _binding: FragmentMyVehicleBinding? = null
    private val binding get() = _binding!!
    private val vmProfile: VmProfile by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyVehicleBinding.inflate(inflater, container, false)
        vmProfile.fetchVehicles()
        inIt()
        return binding.root
    }

    private fun inIt() {
        setOnClickListener()
        setUpObserver()
    }

    private fun setOnClickListener() {
        binding.backArrow.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnAddVehicle.setOnClickListener {
            findNavController().navigate(R.id.action_myVehicleFragment_to_addVehicleFragment)
        }
    }

    private fun setUpObserver() {
        vmProfile.fetchVehicles.observe(viewLifecycleOwner) {
            when (it) {
                is DataState.Success -> {
                    val vehiclesList = it.data
                    if (!vehiclesList.isNullOrEmpty()) {
                        binding.lyNoCars.gone()
                        binding.rvVehicles.visible()
                        binding.rvVehicles.adapter = AdapterMyVehicles(vehiclesList)
                    } else {
                        binding.lyNoCars.visible()
                        binding.rvVehicles.gone()
                    }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}