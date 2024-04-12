package com.example.carbuddy.ui.fragments.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import androidx.appcompat.widget.SearchView
import com.example.carbuddy.R
import com.example.carbuddy.data.models.service.ModelService
import com.example.carbuddy.databinding.FragmentHomeBinding
import com.example.carbuddy.preferences.PreferenceManager
import com.example.carbuddy.utils.Glide
import com.example.carbuddy.utils.gone
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment :Fragment() {
    private lateinit var binding: FragmentHomeBinding
    @Inject
    lateinit var preferenceManager: PreferenceManager
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, null, false)
        inIt()
        return binding.root
    }

    private fun inIt() {
        setUpProfileImage()
        setUpServicesAdapter()
        setUpSearch()
    }

    private fun setUpProfileImage() {
        val user = preferenceManager.getUserData()
        user?.let {
            binding.tvUserName.text = it.fullName
            Glide.loadImageWithListener(requireContext(), user.profileImageUri, binding.imgUser) {
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
                return true
            }
        })
    }

    private fun setUpServicesAdapter() {
        val servicesList = listOf(
            ModelService(R.drawable.img, "Husnain Rafique", "10km away", "Painter"),
        )
        binding.rvHomeServices.adapter = AdapterServices(servicesList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
    }
}