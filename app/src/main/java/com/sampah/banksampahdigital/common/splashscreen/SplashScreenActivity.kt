package com.sampah.banksampahdigital.common.splashscreen

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.sampah.banksampahdigital.databinding.ActivitySplashScreenBinding
import com.sampah.banksampahdigital.common.onboarding.OnboardingActivity

@Suppress("DEPRECATION")
@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    private val SPLASH_TIMEOUT = 2000L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.progressBar.visibility = View.VISIBLE

        Handler().postDelayed({
            binding.progressBar.visibility = View.GONE
            val intent = Intent(this, OnboardingActivity::class.java)
            startActivity(intent)
            finish()
        }, SPLASH_TIMEOUT)

    }
}