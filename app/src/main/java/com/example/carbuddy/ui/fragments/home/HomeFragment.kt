@file:Suppress("DEPRECATION")

package com.example.carbuddy.ui.fragments.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.carbuddy.R
import com.example.carbuddy.data.models.ModelVendorProfile
import com.example.carbuddy.databinding.FragmentHomeBinding
import com.example.carbuddy.preferences.PreferenceManager
import com.example.carbuddy.utils.AccessToken
import com.example.carbuddy.utils.BackPressedExtensions.goBackPressed
import com.example.carbuddy.utils.DataState
import com.example.carbuddy.utils.Glide
import com.example.carbuddy.utils.PermissionUtils
import com.example.carbuddy.utils.gone
import com.example.carbuddy.utils.toast
import com.example.carbuddy.utils.visible
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    @Inject
    lateinit var preferenceManager: PreferenceManager
    private lateinit var adapterServices: AdapterServices
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private val vmHome: VmHome by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, null, false)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        inIt()
        return binding.root
    }

    private fun inIt() {
        askPermissions()
        initLocationRequest()
        setUpLocationCallback()
        getCurrentLocationAndSetupAdapter()
        setUpSearch()
        goBack()
        setUpProfileImage()
        setOnClickListener()
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

    @SuppressLint("MissingPermission")
    private fun getCurrentLocationAndSetupAdapter() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                val currentLatLng = LatLng(it.latitude, it.longitude)
                setUpObserver(currentLatLng)
            } ?: run {
                setUpObserver(null)
            }
        }.addOnFailureListener {
            Toast.makeText(context, "Failed to get current location", Toast.LENGTH_SHORT).show()
            setUpObserver(null)
        }
    }

    private fun setUpObserver(currentLatLng: LatLng?) {
        val progressView = binding.progressBar
        val shimmerLayout = binding.layoutShimmer
        vmHome.mechanicsProfiles.observe(viewLifecycleOwner) {
            when (it) {
                is DataState.Success -> {
                    val listProfiles = it.data
                    if (listProfiles != null) {
                        setUpVendorsAdapter(listProfiles, currentLatLng)
                    }
                    shimmerLayout.stopShimmer()
                    shimmerLayout.gone()
                    binding.rvHomeServices.visible()
                }

                is DataState.Error -> {
                    toast(it.errorMessage)
                    shimmerLayout.stopShimmer()
                    shimmerLayout.gone()
                    binding.rvHomeServices.visible()
                }

                is DataState.Loading -> {
                    shimmerLayout.startShimmer()
                }
            }
        }
    }

    private fun setUpVendorsAdapter(listVendors: List<ModelVendorProfile>, currentLatLng: LatLng?) {
        adapterServices = AdapterServices(listVendors, currentLatLng)
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

    @SuppressLint("MissingPermission")
    private fun initLocationRequest() {
        locationRequest = LocationRequest.create().apply {
            interval = 3000
            fastestInterval = 2000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    private fun setUpLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {

            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val client: SettingsClient = LocationServices.getSettingsClient(requireActivity())
        val task = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                null
            )
        }

        task.addOnFailureListener { e ->
            val statusCode = (e as ResolvableApiException).statusCode
            if (statusCode == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                try {
                    e.startResolutionForResult(requireActivity(), 100)
                } catch (sendEx: IntentSender.SendIntentException) {

                }
            }
        }
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
    }
}