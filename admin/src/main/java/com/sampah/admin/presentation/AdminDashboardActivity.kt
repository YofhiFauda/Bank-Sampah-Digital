package com.sampah.admin.presentation

import android.annotation.SuppressLint
import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sampah.admin.R
import com.sampah.admin.databinding.ActivityAdminDashboardBinding

class AdminDashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminDashboardBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

        setupView()

        binding.btnKeluar.setOnClickListener {
            firebaseAuth.signOut()
            finish()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupView() {
        val userId = firebaseAuth.currentUser

        if (userId != null){
            firestore.collection("admin")
                .document(userId.uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        // Dokumen ditemukan, ambil data pengguna
                        val username = document.getString("username")
                        val email = document.getString("email")

                        binding.tvWelcome.text = "Selamat Datang di Halaman Admin"
                        binding.tvTitle.text = "Username: $username"
                        binding.tvEmail.text = "Email: $email"

                        // Lakukan apa pun dengan data pengguna yang diperoleh
                        Log.d(ContentValues.TAG, "Username: $username, Email: $email")
                    } else {
                        Log.d(ContentValues.TAG, "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    // Gagal mengambil data pengguna
                    Log.d(ContentValues.TAG, "get failed with ", exception)
                }
        }
    }
}