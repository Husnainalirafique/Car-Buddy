package com.example.carbuddy.ui.fragments.bookings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.carbuddy.databinding.FragmentBookingsBinding
import com.example.carbuddy.ui.fragments.bookings.viewpager.BookingsPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class BookingsFragment : Fragment() {
    private var _binding: FragmentBookingsBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookingsBinding.inflate(inflater, container, false)
        inIt()
        return binding.root
    }

    private fun inIt() {
        setUpViewPager()
    }

    private fun setUpViewPager() {
        val adapter = BookingsPagerAdapter(requireActivity())
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Pending"
                1 -> tab.text = "Ongoing"
                2 -> tab.text = "History"
            }
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


