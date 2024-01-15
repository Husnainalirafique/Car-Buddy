package com.example.carbuddy.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.carbuddy.databinding.ActivityAuthBinding
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

    }

    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
    }
}