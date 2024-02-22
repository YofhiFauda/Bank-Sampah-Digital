package com.sampah.banksampahdigital.presentation.common

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sampah.banksampahdigital.R
import com.sampah.banksampahdigital.databinding.ActivityMainBinding
import com.sampah.banksampahdigital.presentation.common.login.LoginActivity
import com.sampah.banksampahdigital.presentation.common.onboarding.OnboardingActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener
    private lateinit var firestore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        authStateListener = FirebaseAuth.AuthStateListener { auth ->
            val user = auth.currentUser
            if (user == null) {
                redirectToLoginScreen()
            }
        }
        setupView()
    }

    @SuppressLint("SetTextI18n")
    private fun setupView() {
        val userId  = firebaseAuth.currentUser

        if (userId != null) {
            // Mengambil data pengguna dari Firestore berdasarkan ID pengguna
            firestore.collection("users").document(userId.uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        // Dokumen ditemukan, ambil data pengguna
                        val username = document.getString("username")
                        val email = document.getString("email")

                        binding.tvUidUser.text = "UID: ${userId.uid}"
                        binding.tvNameUser.text = "Username: $username"
                        binding.tvEmailUser.text = "Email: $email"


                        // Lakukan apa pun dengan data pengguna yang diperoleh
                        Log.d(TAG, "Username: $username, Email: $email")
                    } else {
                        Log.d(TAG, "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    // Gagal mengambil data pengguna
                    Log.d(TAG, "get failed with ", exception)
                }
        }

            binding.signoutButton.setOnClickListener {
                firebaseAuth.signOut()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
    }

    private fun redirectToLoginScreen() {
        val intent = Intent(this, OnboardingActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    override fun onStart() {
        super.onStart()
        firebaseAuth.addAuthStateListener(authStateListener)
    }

    override fun onStop() {
        super.onStop()
        firebaseAuth.removeAuthStateListener(authStateListener)
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}