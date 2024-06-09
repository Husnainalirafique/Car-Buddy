package com.example.carbuddy.ui.fragments.home

import android.Manifest
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
import com.example.carbuddy.utils.PermissionUtils
import com.example.carbuddy.utils.gone
import com.example.carbuddy.utils.toast
import com.example.carbuddy.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    @Inject
    lateinit var preferenceManager: PreferenceManager
    private lateinit var adapterServices: AdapterServices
    private val vmHome: VmHome by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, null, false)
        inIt()
        return binding.root
    }

    private fun inIt() {
        askPermissions()
        setUpObserver()
        setUpProfileImage()
        setUpSearch()
        setOnClickListener()
        goBack()
    }

    private fun askPermissions() {
        val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
        val isPermissionGranted =
            PermissionUtils.handlePermissions(requireActivity(), permissions, 1)
        if (isPermissionGranted) return
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
        val progressView = binding.progressBar
        vmHome.mechanicsProfiles.observe(viewLifecycleOwner) {
            when (it) {
                is DataState.Success -> {
                    val listProfiles = it.data
                    if (listProfiles != null) {
                        setUpVendorsAdapter(listProfiles)
                    }
                    progressView.gone()
                }

                is DataState.Error -> {
                    toast(it.errorMessage)
                    progressView.gone()
                }

                is DataState.Loading -> {
                    progressView.visible()
                }
            }
        }
    }

    private fun setUpVendorsAdapter(listVendors: List<ModelVendorProfile>) {
        adapterServices = AdapterServices(listVendors)
        binding.rvHomeServices.apply {
            setHasFixedSize(true)
            adapter = adapterServices
        }
        adapterServices.itemClickListener {
            val bundle = Bundle()
            bundle.putParcelable("vendorProfile", it)
            findNavController().navigate(R.id.action_homeFragment_to_vendorProfileFragment, bundle)
        }
    }

    private fun setUpProfileImage() {
        preferenceManager.getUserData()?.let {
            binding.tvUserName.text = it.fullName
            Glide.loadImageWithListener(requireContext(), it.profileImageUri, binding.imgUser) {
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
                adapterServices.getFilter().filter(newText)
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