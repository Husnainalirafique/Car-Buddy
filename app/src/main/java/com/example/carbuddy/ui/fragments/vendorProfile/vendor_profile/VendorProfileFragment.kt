@file:Suppress("DEPRECATION")

package com.example.carbuddy.ui.fragments.vendorProfile.vendor_profile

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.carbuddy.R
import com.example.carbuddy.data.models.vendor.ModelVendorProfile
import com.example.carbuddy.databinding.FragmentVendorProfileBinding
import com.example.carbuddy.preferences.PreferenceManager
import com.example.carbuddy.ui.fragments.vendorProfile.viewmodels.VmVendor
import com.example.carbuddy.utils.ClipBoardUtils.copyTextToClipboard
import com.example.carbuddy.utils.Constants
import com.example.carbuddy.utils.DataState
import com.example.carbuddy.utils.Dialogs
import com.example.carbuddy.utils.Glide
import com.example.carbuddy.utils.MapUtils
import com.example.carbuddy.utils.ProgressDialogUtil.dismissProgressDialog
import com.example.carbuddy.utils.ProgressDialogUtil.showProgressDialog
import com.example.carbuddy.utils.invisible
import com.example.carbuddy.utils.onClick
import com.example.carbuddy.utils.toast
import com.example.carbuddy.utils.visible
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class VendorProfileFragment : Fragment() {
    private var _binding: FragmentVendorProfileBinding? = null
    private val binding get() = _binding!!
    private var isLiked = false
    private val viewModel: VmVendor by viewModels()
    private var totalLikes: Long = 0
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    @Inject
    lateinit var prefs: PreferenceManager
    private lateinit var vendorUid: String


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVendorProfileBinding.inflate(inflater, container, false)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        inIt()
        return binding.root
    }

    private fun inIt() {
        initLocationRequest()
        setUpLocationCallback()
        setOnClickListener()
        getAndSetDataForVendor()
        setUpLikeButton()
        setUpObserver()
    }

    private fun setOnClickListener() {
        binding.backArrow.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun getAndSetDataForVendor() {
        val vendorProfile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("vendorProfile", ModelVendorProfile::class.java)
        } else {
            arguments?.getParcelable("vendorProfile")
        }

        vendorProfile?.let {
            Glide.loadImage(requireContext(), it.vendorImage, binding.imgUser)
            binding.tvVendorName.text = it.fullName
            binding.tvExperience.text = it.yearsOfExperience
            binding.tvCity.text = it.city
            binding.tvLocation.text = it.locationNameInCity
            binding.tvShopName.text = it.shopName
            binding.tvAvailablity.text = it.availability
            vendorUid = it.vendorUid

            val bundleBook = Bundle()
            bundleBook.putParcelable("vendorProfile", it)
            binding.btnBookNow.onClick {
                findNavController().navigate(
                    R.id.action_vendorProfileFragment_to_mechanicBookingDetailsFragment,
                    bundleBook
                )
            }

            val (latitude, longitude) = MapUtils.extractLatLong(it.addressFromMap)!!
            binding.btnShowMap.setOnClickListener {
                openMapAndGiveDirections(latitude, longitude)
            }

            val bundle = Bundle()
            bundle.putString(Constants.KEY_PROVIDER_UID, it.vendorUid)
            clickListenerToComments(bundle)

            binding.btnContact.setOnClickListener { _ ->
                Dialogs.dialogContactViaPhone(
                    context = requireContext(),
                    inflater = layoutInflater,
                    copy = { copy(it.contactNumber) },
                    callUs = { makePhoneCall(it.contactNumber) },
                    openWhatsapp = { openWhatsAppChat(it.whatsappNumber) }
                )
            }
        }
    }


    private fun clickListenerToComments(bundle: Bundle) {
        binding.btnComments.setOnClickListener {
            findNavController().navigate(
                R.id.action_vendorProfileFragment_to_commentsFragment,
                bundle
            )
        }
    }

    private fun setUpLikeButton() {
        lifecycleScope.launch {
            viewModel.fetchTotalLikes(vendorUid)
        }

        isLiked = prefs.isProfileLiked(vendorUid)
        updateLikeButton(isLiked)

        binding.btnLike.setOnClickListener {
            isLiked = !isLiked
            updateLikeButton(isLiked)
            viewModel.updateLike(isLiked, vendorUid)
        }
    }

    private fun updateLikeButton(isLiked: Boolean) {
        val iconLike = if (isLiked) R.drawable.icon_heart_filled_red else R.drawable.icon_heart_thin
        binding.btnLike.setImageResource(iconLike)

        val likedText = "${if (isLiked) totalLikes + 1 else maxOf(0, totalLikes - 1)} Likes"

        binding.tvLikes.text = likedText
        prefs.setProfileLiked(vendorUid, isLiked)
    }

    @SuppressLint("SetTextI18n")
    private fun setUpObserver() {
        viewModel.totalLikes.observe(viewLifecycleOwner) {
            when (it) {
                is DataState.Success -> {
                    val totalLikes = it.data
                    if (totalLikes != null) {
                        this.totalLikes = totalLikes
                        binding.tvLikes.text = "$totalLikes Likes"
                    }
                    dismissProgressDialog()
                    binding.layoutVendor.visible()
                }

                is DataState.Error -> {
                    toast(it.errorMessage)
                    dismissProgressDialog()
                    binding.layoutVendor.visible()
                }

                is DataState.Loading -> {
                    binding.layoutVendor.invisible()
                    showProgressDialog()
                }
            }
        }
    }

    private fun copy(text: String) {
        copyTextToClipboard(text)
    }

    private fun makePhoneCall(number: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$number")
        startActivity(intent)
    }

    private fun openWhatsAppChat(number: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("https://wa.me/$number")
        startActivity(intent)
    }

    //Getting user current location
    @SuppressLint("MissingPermission")
    private fun openMapAndGiveDirections(lat: Double, lng: Double) {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                val currentLatLng = LatLng(it.latitude, it.longitude)
                val destinationLatLng = LatLng(lat, lng)

                // Construct URI for directions
                val uri =
                    "http://maps.google.com/maps?saddr=${currentLatLng.latitude},${currentLatLng.longitude}&daddr=${destinationLatLng.latitude},${destinationLatLng.longitude}"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                intent.setPackage("com.google.android.apps.maps")
                startActivity(intent)
            } ?: run {
                Toast.makeText(context, "Unable to get current location", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(context, "Failed to get current location", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initLocationRequest() {
        locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
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

        task.addOnFailureListener {
            Toast.makeText(context, "Please enable location settings", Toast.LENGTH_SHORT).show()
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
        _binding = null
    }
}