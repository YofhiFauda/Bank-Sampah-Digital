package com.sampah.banksampahdigital.common.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sampah.admin.presentation.AdminDashboardActivity
import com.sampah.banksampahdigital.MainActivity
import com.sampah.banksampahdigital.R
import com.sampah.banksampahdigital.databinding.ActivityLoginBinding
import com.sampah.banksampahdigital.common.register.RegistrasiActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener
    private lateinit var firestore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        authStateListener = FirebaseAuth.AuthStateListener { auth ->
            val user = auth.currentUser
            if (user != null) {
                redirectToLoginScreen()
                Log.e(user.uid, "Success Login UID: ${user.uid}")
            }
        }

        setupAction()
        setupLogin()
    }


    private fun redirectToLoginScreen(){
        val intent = Intent(this, MainActivity::class.java)
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

    private fun setupAction(){
        binding.tvDaftar.setOnClickListener {
            startActivity(Intent(this, RegistrasiActivity::class.java))
            finish()
        }
    }

    private fun setupLogin(){
        binding.btnLogin.setOnClickListener {
            val email = binding.edLoginEmail.text.toString().trim()
            val password = binding.edLoginPassword.text.toString().trim()

            when{
                email.isEmpty() -> {
                    binding.edLoginEmail.error = resources.getString(R.string.message_validation, "email")
                }
                password.isEmpty() -> {
                    binding.edLoginPassword.error = resources.getString(R.string.message_validation, "password")
                }

                else -> {
                    if (email.isNotEmpty() && password.isNotEmpty()){
                        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { loginTask ->
                            if (loginTask.isSuccessful) {
                                val user = firebaseAuth.uid

                                if (user != null) {
                                    firestore.collection("users").document(user).get().addOnCompleteListener { userTask ->
                                        if (userTask.isSuccessful && userTask.result != null && userTask.result.exists()) {
                                            startActivity(Intent(this, MainActivity::class.java))
                                            finish()
                                        } else {
                                            // Cek apakah email pengguna ada di koleksi 'admin'
                                            firestore.collection("admin").document(user).get().addOnCompleteListener { adminTask ->
                                                if (adminTask.isSuccessful && adminTask.result != null && adminTask.result.exists()) {
                                                    // Pengguna dengan email yang login terdapat di koleksi 'admin'
                                                    // Simpan informasi bahwa pengguna adalah admin
                                                    val sharedPref = getSharedPreferences("user_type", Context.MODE_PRIVATE)
                                                    with (sharedPref.edit()) {
                                                        putBoolean("isAdmin", true)
                                                        apply()
                                                    }
                                                    // Redirect ke halaman dashboard admin
                                                    startActivity(Intent(this, AdminDashboardActivity::class.java))
                                                    finish()
                                                } else {
                                                    // Jika tidak ada di koleksi 'user' maupun 'admin'
                                                    Toast.makeText(this@LoginActivity, "User not found", Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    // Gagal mendapatkan email pengguna
                                    Toast.makeText(this@LoginActivity, "Failed to get user email", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                // Gagal login
                                Toast.makeText(this@LoginActivity, "Please Try Again", Toast.LENGTH_SHORT).show()
                            }
                        }

                    }
                    else {
                        Toast.makeText(this, resources.getString(R.string.login_error), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
