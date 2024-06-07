package com.example.carbuddy.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.carbuddy.R
import com.example.carbuddy.data.models.ModelVendorProfile
import com.example.carbuddy.databinding.FragmentHomeBinding
import com.example.carbuddy.preferences.PreferenceManager
import com.example.carbuddy.utils.BackPressedExtensions.goBackPressed
import com.example.carbuddy.utils.DataState
import com.example.carbuddy.utils.Glide
import com.example.carbuddy.utils.ProgressDialogUtil.dismissProgressDialog
import com.example.carbuddy.utils.ProgressDialogUtil.showProgressDialog
import com.example.carbuddy.utils.gone
import com.example.carbuddy.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment :Fragment() {
    private lateinit var binding: FragmentHomeBinding
    @Inject
    lateinit var preferenceManager: PreferenceManager
    private val vmHome: VmHome by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, null, false)
        inIt()
        return binding.root
    }

    private fun inIt() {
        setUpObserver()
        setUpProfileImage()
        setUpSearch()
        setOnClickListener()
        goBack()
    }

    private fun setOnClickListener() {
        binding.btnBookmark.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_bookMarkFragment)
        }
        binding.btnMechanics.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_mechanicsFragment)
        }
        binding.btnFuel.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_fuelFragment)
        }
        binding.btnServiceShops.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_serviceShopsFragment)
        }
    }

    private fun setUpObserver() {
        vmHome.mechanicsProfiles.observe(viewLifecycleOwner) {
            when (it) {
                is DataState.Success -> {
                    val listProfiles = it.data
                    if (listProfiles != null) {
                        setUpVendorsAdapter(listProfiles)
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

    private fun setUpVendorsAdapter(listVendors: List<ModelVendorProfile>) {
        val adapterServices = AdapterServices(listVendors)
        binding.rvHomeServices.adapter = adapterServices
        adapterServices.itemClickListener {
            val bundle = Bundle()
            bundle.putParcelable("vendorProfile", it)
            findNavController().navigate(R.id.action_homeFragment_to_vendorProfileFragment, bundle)
        }
    }

    private fun setUpProfileImage() {
        val user = preferenceManager.getUserData()
        user?.let {
            binding.tvUserName.text = it.fullName
            Glide.loadImageWithListener(requireContext(), user.profileImageUri, binding.imgUser) {
                binding.pgProfileImage.gone()
            }
        }
    }

    private fun setUpSearch() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    private fun goBack() {
        goBackPressed {
            requireActivity().finishAffinity()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
    }
}