package com.example.carbuddy.ui.fragments.bookings.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.carbuddy.databinding.FragmentHistoryBinding
import com.example.carbuddy.ui.fragments.bookings.VmBooking
import com.example.carbuddy.ui.fragments.bookings.adapter.AdapterBooking
import com.example.carbuddy.utils.DataState
import com.example.carbuddy.utils.ProgressDialogUtil.dismissProgressDialog
import com.example.carbuddy.utils.ProgressDialogUtil.showProgressDialog
import com.example.carbuddy.utils.gone
import com.example.carbuddy.utils.toast
import com.example.carbuddy.utils.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryFragment : Fragment() {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private val vmBooking: VmBooking by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        vmBooking.getCompletedBookings()
        inIt()
        return binding.root
    }

    private fun inIt() {
        setUpObserver()
    }

    private fun setUpObserver() {
        vmBooking.completedBookings.observe(viewLifecycleOwner) {
            when (it) {
                is DataState.Success -> {
                    val bookingList = it.data
                    if (bookingList.isNullOrEmpty()) {
                        binding.layoutNoBookings.visible()
                        binding.recyclerView.gone()
                    } else {
                        binding.layoutNoBookings.gone()
                        binding.recyclerView.visible()
                        val adapter = AdapterBooking(bookingList)
                        binding.recyclerView.adapter = adapter
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