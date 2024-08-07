package com.sampah.common.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sampah.common.R
import com.sampah.common.databinding.ActivityLoginBinding
import com.sampah.common.registrasi.RegistrasiActivity

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
                redirectToDashboard()
                Log.e(user.uid, "Success Login UID: ${user.uid}")
            }
        }

        setupAction()
        setupLogin()
    }


    private fun redirectToDashboard() {
        val user = firebaseAuth.currentUser
        if (user != null) {
            val userRef = firestore.collection("users").document(user.uid)
            val adminRef = firestore.collection("admin").document(user.uid)

            // Periksa koleksi "users" terlebih dahulu
            userRef.get().addOnSuccessListener { document ->
                if (document.exists()) {
                    // Jika ada di koleksi "users"
                    val intent = Intent()
                    intent.setClassName(this, "com.sampah.user.MainActivity")
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                } else {
                    // Jika tidak ada di koleksi "users", periksa koleksi "admins"
                    adminRef.get().addOnSuccessListener { adminDocument ->
                        if (adminDocument.exists()) {
                            // Jika ada di koleksi "admins"
                            val intent = Intent()
                            intent.setClassName(this, "com.sampah.admin.AdminActivity")
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                        } else {
                            // Jika tidak ditemukan di kedua koleksi
                            Toast.makeText(this, "User does not exist in either collection", Toast.LENGTH_SHORT).show()
                        }
                    }.addOnFailureListener { e ->
                        Toast.makeText(this, "Failed to get admin data: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }.addOnFailureListener { e ->
                Toast.makeText(this, "Failed to get user data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun setupLogin() {
        binding.btnLogin.setOnClickListener {
            val email = binding.edLoginEmail.text.toString().trim()
            val password = binding.edLoginPassword.text.toString().trim()

            when {
                email.isEmpty() -> {
                    binding.edLoginEmail.error = resources.getString(R.string.message_validation, "email")
                }
                password.isEmpty() -> {
                    binding.edLoginPassword.error = resources.getString(R.string.message_validation, "password")
                }
                else -> {
                    if (email.isNotEmpty() && password.isNotEmpty()) {
                        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { loginTask ->
                            if (loginTask.isSuccessful) {
                                Toast.makeText(this, "Login Success ${loginTask.isSuccessful}", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this, "Login failed: ${loginTask.exception?.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(this, resources.getString(R.string.login_error), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
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
}