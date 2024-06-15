package com.sampah.banksampahdigital.ui.onboarding

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.sampah.banksampahdigital.databinding.ActivityOnboardingBinding
import com.sampah.banksampahdigital.R
import com.sampah.banksampahdigital.ui.login.LoginActivity

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    var onboardingAdapter: OnboardingAdapter? = null
    var tabLayout: TabLayout? = null
    var screenViewPager: ViewPager? = null
    var position = 0
    var sharedPreferences: SharedPreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tabLayout = binding.tabIndicator

        val onboardingItem: MutableList<OnboardingItem> = ArrayList()
        onboardingItem.add(OnboardingItem("Mulailah Menabung\nSampah, Mulailah\nMengubah Dunia!","Mulailah Menabung Sampah dan Mengubah Dunia, Satu Sampah di Waktu, dengan Aplikasi Kami yang Memungkinkan Anda Mendapatkan Poin untuk Setiap Kontribusi Lingkungan yang Anda Buat!", R.drawable.onboarding_screen1))
        onboardingItem.add(OnboardingItem("Menyumbang Sampah,\nMenyelamatkan\nLingkungan","Melalui Tindakan Sederhana Menyumbangkan Sampah, Anda Berpartisipasi dalam Misi Menyelamatkan Lingkungan dan Mengurangi Limbah. Bersama-sama Kita Mampu Mencapai Perubahan Besar untuk Lingkungan Kita",R.drawable.onboarding_screen2))
        onboardingItem.add(OnboardingItem("Membuat Dunia Lebih\nBaik, Langkah Demi\nLangkah!","Membuat Dunia Menjadi Tempat yang Lebih Baik adalah Proses yang Panjang, Namun Setiap Langkah Kecil yang Anda Ambil Membawa Kita Lebih Dekat pada Tujuan Itu",R.drawable.onboarding_screen3))

        setupView(onboardingItem)

        if (restorePrefData()){
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
        }

        position = screenViewPager!!.currentItem

        binding.btnNext.setOnClickListener {
            if (position < onboardingItem.size) {
                position++
                screenViewPager!!.currentItem = position
            }
            if (position == onboardingItem.size) {
                savePrefData()
                val intent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent)
            }
        }
        tabLayout!!.addOnTabSelectedListener(object : OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                position = tab!!.position
                if(tab.position == onboardingItem.size - 1){
                    binding.btnNext.text = "Get Started"
                }else{
                    binding.btnNext.text = "Next"
                }
            }
        })
    }

    private fun setupView(onboardingItem: List<OnboardingItem>) {

        screenViewPager = binding.screenViewPager
        onboardingAdapter = OnboardingAdapter(this, onboardingItem)
        screenViewPager!!.adapter = onboardingAdapter
        tabLayout?.setupWithViewPager(screenViewPager)

    }

    private fun savePrefData(){
        sharedPreferences = applicationContext.getSharedPreferences("pref", MODE_PRIVATE)
        val editor = sharedPreferences!!.edit()
        editor.putBoolean("isFirstTimeRun", true)
        editor.apply()
    }

    private fun restorePrefData(): Boolean{
        sharedPreferences = applicationContext.getSharedPreferences("pref", MODE_PRIVATE)
        return sharedPreferences!!.getBoolean("isFirstTimeRun", false)
    }


}