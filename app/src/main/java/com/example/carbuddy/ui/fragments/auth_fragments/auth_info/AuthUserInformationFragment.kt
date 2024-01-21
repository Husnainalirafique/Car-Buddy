package com.example.carbuddy.ui.fragments.auth_fragments.auth_info

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.carbuddy.R
import com.example.carbuddy.databinding.FragmentAuthUserInformationBinding

class AuthUserInformationFragment :Fragment() {
    private lateinit var binding: FragmentAuthUserInformationBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAuthUserInformationBinding.inflate(layoutInflater, null, false)
        inIt()
        return binding.root
    }

    private fun inIt() {
        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.backArrow.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
    }
}