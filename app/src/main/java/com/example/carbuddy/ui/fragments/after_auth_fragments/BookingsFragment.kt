package com.example.carbuddy.ui.fragments.after_auth_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.example.carbuddy.R
import com.example.carbuddy.databinding.FragmentBookingsBinding
import com.example.carbuddy.utils.dataBinding

class BookingsFragment : Fragment(R.layout.fragment_bookings) {
    private val binding by dataBinding(FragmentBookingsBinding::inflate)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inIt()
    }

    private fun inIt() {
        setOnClickListener()
    }

    private fun setOnClickListener() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
    }
}


