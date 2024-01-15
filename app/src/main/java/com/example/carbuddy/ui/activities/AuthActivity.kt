package com.example.carbuddy.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.carbuddy.databinding.ActivityAuthBinding
import com.example.carbuddy.utils.BackPressedExtensions
import com.example.carbuddy.utils.BackPressedExtensions.goBackPressed
import com.example.carbuddy.utils.StatusBarUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {
    private val binding by lazy { ActivityAuthBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtils.setStatusBarDarkMode(this)
        setContentView(binding.root)
        inIt()
    }

    private fun inIt() {
        backPressed()
    }

    private fun backPressed() {
        goBackPressed {
            finishAffinity()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
    }
}