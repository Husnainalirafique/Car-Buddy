package com.example.carbuddy.ui.fragments.bookingFlow

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.carbuddy.data.NotificationData
import com.example.carbuddy.data.models.ModelVendorProfile
import com.example.carbuddy.databinding.FragmentMechanicBookingDetailsBinding
import com.example.carbuddy.utils.onClick
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MechanicBookingDetailsFragment : Fragment() {
    private var _binding: FragmentMechanicBookingDetailsBinding? = null
    private val binding get() = _binding!!
    private val vm: NotificationViewModel by viewModels()


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
    }

    private fun setOnClickListener() {

    }

    private fun getAndSetDataForVendor() {
        val vendorProfile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("vendorProfile", ModelVendorProfile::class.java)
        } else {
            arguments?.getParcelable("vendorProfile")
        }

        vendorProfile?.let { vendor ->
            binding.btnConformBooking.onClick {
                lifecycleScope.launch(Dispatchers.IO) {
                    val data = NotificationData("You got a new order 😍", "Hi ${vendor.fullName}")
                    vm.sendNotification(vendor.fcmToken, data)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
