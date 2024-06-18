package com.example.carbuddy.ui.fragments.bookingFlow

import android.R
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.carbuddy.data.models.booking.ModelBookingData
import com.example.carbuddy.data.models.vehicles.ModelVehicle
import com.example.carbuddy.data.models.vendor.ModelVendorProfile
import com.example.carbuddy.databinding.FragmentMechanicBookingDetailsBinding
import com.example.carbuddy.preferences.PreferenceManager
import com.example.carbuddy.ui.activities.MapActivity
import com.example.carbuddy.utils.DataState
import com.example.carbuddy.utils.ProgressDialogUtil.dismissProgressDialog
import com.example.carbuddy.utils.ProgressDialogUtil.showProgressDialog
import com.example.carbuddy.utils.ifEmpty
import com.example.carbuddy.utils.onClick
import com.example.carbuddy.utils.toast
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@Suppress("DEPRECATION")
@AndroidEntryPoint
class MechanicBookingDetailsFragment : Fragment() {
    private var _binding: FragmentMechanicBookingDetailsBinding? = null
    private val binding get() = _binding!!
    private val vm: NotificationViewModel by viewModels()

    @Inject
    lateinit var prefs: PreferenceManager

    @Inject
    lateinit var auth: FirebaseAuth
    private var profileLocationLatLng: String? = null
    private lateinit var modelVehicle: ModelVehicle


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMechanicBookingDetailsBinding.inflate(inflater, container, false)
        inIt()
        return binding.root
    }

    private fun inIt() {
        setOnClickListener()
        getAndSetDataForVendor()
        setUpObserver()
    }

    private fun setUpObserver() {
        vm.bookingStatus.observe(viewLifecycleOwner) {
            when (it) {
                is DataState.Success -> {
                    toast("Success")
                    binding.etAddress.text?.clear()
                    binding.etDetail.text?.clear()
                    profileLocationLatLng = null
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
        vm.fetchVehicles.observe(viewLifecycleOwner) { dataState ->
            when (dataState) {
                is DataState.Success -> {
                    val carsList = dataState.data
                    if (carsList != null) {
                        val carNames =
                            carsList.map { "${it.model}, ${it.make}, ${it.year}, ${it.lpn}" }
                        binding.spinnerSelectCar.setOnClickListener {
                            val arrayAdapter = ArrayAdapter(
                                requireContext(),
                                R.layout.simple_dropdown_item_1line,
                                carNames
                            )
                            binding.spinnerSelectCar.setAdapter(arrayAdapter)
                            binding.spinnerSelectCar.showDropDown()
                        }
                    }
                }

                is DataState.Error -> {
                    toast(dataState.errorMessage)
                }

                is DataState.Loading -> {

                }
            }
        }
    }

    private fun setOnClickListener() {
        binding.btnChooseLocationOnMap.onClick {
            val intent = Intent(requireContext(), MapActivity::class.java)
            startMapActivityForResult.launch(intent)
        }
    }

    private fun getAndSetDataForVendor() {
        val vendorProfile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("vendorProfile", ModelVendorProfile::class.java)
        } else {
            arguments?.getParcelable("vendorProfile")
        }

        val user = prefs.getUserData()
        if (user != null && vendorProfile != null)
            binding.btnConformBooking.onClick {
                if (validate()) {
                    val address = binding.etAddress.text.toString().trim()
                    val detail = binding.etDetail.text.toString().trim()
                    val selectedVehicleText = binding.spinnerSelectCar.text.toString()
                    val (model, make, year, lpn) = selectedVehicleText.split(", ").run {
                        arrayOf(this[0], this[1], this[2], this[3])
                    }

                    val bookingData = ModelBookingData(
                        address = address,
                        detail = detail,
                        latLng = profileLocationLatLng!!,
                        userFcmToken = user.fcmToken,
                        userUid = auth.currentUser?.uid!!,
                        vendorUid = vendorProfile.vendorUid,
                        bookingStatus = "Requested",
                        userImageUri = user.profileImageUri,
                        bookingTypeTag = "Vehicle Repairing",
                        userName = user.fullName,
                        vehicleModel = model,
                        vehicleMake = make,
                        vehicleYear = year,
                        vehicleLpn = lpn,
                        accepted = false
                    )
                    conformBooking(bookingData, vendorProfile)
                }
            }
    }

    private fun conformBooking(
        bookingData: ModelBookingData,
        vendor: ModelVendorProfile
    ) {
        lifecycleScope.launch(Dispatchers.IO) {
            vm.saveBookingToDb(bookingData, vendor)
        }
    }

    //to get the location latLng
    private val startMapActivityForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                Activity.RESULT_OK -> {
                    val data: Intent? = result.data
                    val latLngString = data?.getStringExtra("LOCATION")
                    if (latLngString != null) {
                        profileLocationLatLng = latLngString
                    }
                }
            }
        }

    private fun validate(): Boolean {
        val etAddress = binding.etAddress
        val etDetail = binding.etDetail
        etAddress.error = null
        etDetail.error = null
        return when {
            etAddress.ifEmpty("Can't be empty!") -> false
            etDetail.ifEmpty("Can't be empty!") -> false

            profileLocationLatLng == null -> {
                toast("Please select a location from map.")
                false
            }

            binding.spinnerSelectCar.text.isNullOrEmpty() -> {
                toast("Select a Vehicle first")
                false
            }

            else -> true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
