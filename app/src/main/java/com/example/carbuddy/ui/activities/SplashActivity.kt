package com.example.carbuddy.ui.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.carbuddy.databinding.ActivitySplashBinding
import com.example.carbuddy.utils.dataBinding
import com.example.carbuddy.utils.startActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        inIt()
    }

    private fun inIt() {
        setUpSplash()
    }


    private fun setUpSplash() {
        lifecycleScope.launch {
            delay(500)
            startActivity(AuthActivity::class.java)
            finish()
        }
    }
}




