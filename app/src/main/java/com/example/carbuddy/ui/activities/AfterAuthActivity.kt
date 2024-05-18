package com.example.carbuddy.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.carbuddy.R
import com.example.carbuddy.data.models.ModelUser
import com.example.carbuddy.data.models.vehicles.ModelVehicle
import com.example.carbuddy.databinding.ActivityAfterAuthBinding
import com.example.carbuddy.ui.fragments.home.HomeFragment
import com.example.carbuddy.utils.BackPressedExtensions.goBackPressed
import com.example.carbuddy.utils.gone
import com.example.carbuddy.utils.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AfterAuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAfterAuthBinding
    private lateinit var navHostFragment: Fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAfterAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navHostFragment = supportFragmentManager.findFragmentById(R.id.afterAuthActivityNavHostFragment)!!
        inIt()
    }

    private fun inIt() {
        setUpBottomBar()
        handleBackPressed()
    }

    @Suppress("DEPRECATION")
    private fun setUpBottomBar() {
        binding.bottomNavigationView.setupWithNavController(navHostFragment.findNavController())

        navHostFragment.findNavController().addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.homeFragment || destination.id == R.id.bookingsFragment || destination.id == R.id.profileFragment) {
                binding.bottomNavigationView.visible()
                window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
            } else {
                binding.bottomNavigationView.gone()
                window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            }
        }
    }

    private fun handleBackPressed() {
        goBackPressed {
            if (navHostFragment.childFragmentManager.fragments.first() is HomeFragment) {
                finishAffinity()
            } else {
                navHostFragment.findNavController().popBackStack()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
    }
}