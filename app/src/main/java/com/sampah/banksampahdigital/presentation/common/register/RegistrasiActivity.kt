package com.sampah.banksampahdigital.presentation.common.register

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sampah.banksampahdigital.databinding.ActivityRegistrasiBinding
import com.sampah.banksampahdigital.presentation.common.login.LoginActivity
import com.sampah.banksampahdigital.R

class RegistrasiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrasiBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrasiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        register()
        setupAction()
    }



    private fun register(){
        binding.btnRegistrasi.setOnClickListener {
            val username = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString().trim()
            val password = binding.edRegisterPassword.text.toString().trim()
            val konfirmPassword = binding.edRegisterKonfirmPassword.text.toString().trim()

            when{
                username.isEmpty() -> {
                    binding.edRegisterName.error = resources.getString(R.string.message_validation, "username")
                }
                email.isEmpty() -> {
                    binding.edRegisterEmail.error = resources.getString(R.string.message_validation, "email")
                }
                password.isEmpty() -> {
                    binding.edRegisterPassword.error = resources.getString(R.string.message_validation, "password")
                }
                konfirmPassword.isEmpty() -> {
                    binding.edRegisterKonfirmPassword.error = resources.getString(R.string.message_validation, "konfirmasi password")
                }
                else -> {
                    if (username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()){
                        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                            if (password == konfirmPassword){
                                if (it.isSuccessful){
                                    AlertDialog.Builder(this@RegistrasiActivity).apply {
                                        setTitle("Yeah!")
                                        setMessage("Your account successfully created!")
                                        setPositiveButton("Next") { _, _ ->
                                            finish()
                                        }
                                        create()
                                        show()
                                    }
                                }
                                else {
                                    Toast.makeText(this, "Please Try Again", Toast.LENGTH_SHORT).show()
                                }
                            }else{
                                Toast.makeText(this, "Please create same password", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    else {
                        Toast.makeText(this, resources.getString(R.string.signup_error), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

    private fun setupAction() {
        binding.tvSignIn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}