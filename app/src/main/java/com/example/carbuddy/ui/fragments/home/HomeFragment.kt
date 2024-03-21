package com.example.carbuddy.ui.fragments.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.carbuddy.R
import com.example.carbuddy.databinding.FragmentHomeBinding

class HomeFragment :Fragment() {
    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, null, false)
        inIt()
        return binding.root
    }

    private fun inIt() {
        setUpSearchByCity()
    }

    private fun setUpSearchByCity() {
        val cities = arrayOf("Khanpur", "Rahim Yar khan", "Liaqatpur")
        val autoCompleteTextView = binding.tvAutoComplete

        val adapter = ArrayAdapter(requireContext(), R.layout.item_drop_down, cities)
        autoCompleteTextView.setAdapter(adapter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
    }
}