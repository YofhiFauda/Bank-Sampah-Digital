package com.sampah.banksampahdigital.common.onboarding

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import com.sampah.banksampahdigital.R
import com.sampah.banksampahdigital.databinding.ActivityOnboardingBinding
import com.sampah.banksampahdigital.common.login.LoginActivity
import com.sampah.banksampahdigital.common.register.RegistrasiActivity

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
    }



    private fun setupAction() {
        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.btnRegistrasi.setOnClickListener {
            startActivity(Intent(this, RegistrasiActivity::class.java))
        }
    }

}