package com.example.carbuddy.ui.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.example.carbuddy.R
import com.example.carbuddy.databinding.ActivitySplashBinding
import com.example.carbuddy.utils.StatusBarUtils
import com.example.carbuddy.utils.startActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySplashBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setUpSplash()
    }

    private fun setUpSplash() {
        lifecycleScope.launch {
            delay(500)
            startActivity(AuthActivity::class.java)
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
    }
}