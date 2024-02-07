package com.example.carbuddy.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.carbuddy.R
import com.example.carbuddy.databinding.ActivityAfterAuthBinding
import com.example.carbuddy.ui.fragments.after_auth_fragments.HomeFragment
import com.example.carbuddy.utils.BackPressedExtensions.goBackPressed
import com.example.carbuddy.utils.StatusBarUtils
import com.example.carbuddy.utils.gone
import com.example.carbuddy.utils.toast
import com.example.carbuddy.utils.visible
import kotlin.system.exitProcess

class AfterAuthActivity : AppCompatActivity() {
    private val binding by lazy { ActivityAfterAuthBinding.inflate(layoutInflater) }
    private var navHostFragment: Fragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtils.setStatusBarDarkMode(this)
        setContentView(binding.root)
        navHostFragment = supportFragmentManager.findFragmentById(R.id.afterAuthActivityNavHostFragment)
        inIt()
    }

    private fun inIt() {
        setUpBottomBar()
        handleBackPressed()
    }

    private fun setUpBottomBar() {
        navHostFragment?.let { binding.bottomNavigationView.setupWithNavController(it.findNavController()) }
        navHostFragment?.findNavController()?.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.homeFragment || destination.id == R.id.bookingsFragment || destination.id == R.id.inboxFragment || destination.id == R.id.profileFragment) {
                binding.bottomNavigationView.visible()
            } else {
                binding.bottomNavigationView.gone()
            }
        }
    }

    private fun handleBackPressed() {
        goBackPressed {
            if (navHostFragment?.childFragmentManager?.fragments?.first() is HomeFragment) {
                finishAffinity()
            } else {
                findNavController(R.id.afterAuthActivityNavHostFragment).popBackStack()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
    }
}