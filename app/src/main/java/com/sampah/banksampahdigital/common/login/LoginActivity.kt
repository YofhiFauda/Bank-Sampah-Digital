package com.sampah.banksampahdigital.common.login

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
import com.sampah.banksampahdigital.user.dashboard.DashboardFragment

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

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
                            if (loginTask.isSuccessful){
                                val currentUser = firebaseAuth.currentUser
                                if (currentUser != null) {
                                    val db = FirebaseFirestore.getInstance()

                                    // Check if the user exists in the "users" collection
                                    db.collection("users").document(currentUser.uid).get().addOnSuccessListener { document ->
                                        if (document.exists()) {
                                            // User exists, redirect to user dashboard
                                            startActivity(Intent(this, DashboardFragment::class.java))
                                            finish()
                                        } else {
                                            // Check if the user exists in the "admins" collection
                                            db.collection("admins").document(currentUser.uid).get().addOnSuccessListener { adminDocument ->
                                                if (adminDocument.exists()) {
                                                    // Admin exists, redirect to admin dashboard
                                                    startActivity(Intent(this, AdminDashboardActivity::class.java))
                                                    finish()
                                                } else {
                                                    // User or admin not found, handle accordingly
                                                    Toast.makeText(this@LoginActivity, "User not found", Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            else {
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
