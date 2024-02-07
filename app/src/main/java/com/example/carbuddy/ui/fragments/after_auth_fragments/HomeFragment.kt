package com.example.carbuddy.ui.fragments.after_auth_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
    }
}