package com.sampah.common.registrasi

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.sampah.common.R
import com.sampah.common.databinding.ActivityRegistrasiBinding
import com.sampah.common.login.LoginActivity

class RegistrasiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrasiBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var verificationRunnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrasiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

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
                                if (it.isSuccessful) {
                                    val currentUser = firebaseAuth.currentUser
                                    val user = hashMapOf(
                                        "username" to username,
                                        "email" to email,
                                        "role" to "user"
                                        // Tambahkan field tambahan sesuai kebutuhan
                                    )

                                    if (currentUser != null) {
                                        val db = FirebaseFirestore.getInstance()

                                        // Buat dokumen baru dengan menggunakan UID pengguna sebagai kunci
                                        db.collection("users").document(currentUser.uid)
                                            .get()
                                            .addOnSuccessListener { document ->
                                                // Periksa apakah dokumen sudah ada
                                                if (document.exists()) {
                                                    // Dokumen sudah ada, mungkin ada tindakan yang perlu diambil
                                                    // Misalnya, memberikan pesan bahwa pengguna sudah terdaftar
                                                    Toast.makeText(this, "User already registered!", Toast.LENGTH_SHORT).show()
                                                } else {
                                                    // Dokumen tidak ada, simpan data pengguna ke Firestore
                                                    db.collection("users").document(currentUser.uid)
                                                        .set(user)
                                                        .addOnSuccessListener {
                                                            // Tampilkan pesan sukses
                                                            Toast.makeText(this@RegistrasiActivity, "Your account successfully created!", Toast.LENGTH_SHORT).show()
                                                            // Kirim email verifikasi
                                                            currentUser.sendEmailVerification()
                                                                .addOnCompleteListener { verificationTask ->
                                                                    if (verificationTask.isSuccessful) {
                                                                        // Tampilkan pesan bahwa email verifikasi telah dikirim
                                                                        AlertDialog.Builder(this).apply {
                                                                            setTitle("Verification Email")
                                                                            setMessage("Email verification sent. Please check your email.")
                                                                            setPositiveButton("OK") { dialog, _ ->
                                                                                dialog.dismiss()
                                                                                startVerificationCheck(currentUser)
                                                                            }
                                                                            create()
                                                                            show()
                                                                        }
                                                                    } else {
                                                                        // Gagal mengirim email verifikasi
                                                                        Toast.makeText(this@RegistrasiActivity, "Failed to send email verification: ${verificationTask.exception?.message}", Toast.LENGTH_SHORT).show()
                                                                        showVerificationFailedDialog(currentUser)
                                                                    }
                                                                }
                                                        }
                                                        .addOnFailureListener { e ->
                                                            // Tampilkan pesan gagal jika data tidak dapat disimpan di Firestore
                                                            Toast.makeText(this, "Failed to save user data: ${e.message}", Toast.LENGTH_SHORT).show()
                                                        }
                                                }
                                            }
                                            .addOnFailureListener { e ->
                                                // Tampilkan pesan gagal jika gagal mengambil dokumen
                                                Toast.makeText(this, "Failed to check existing user: ${e.message}", Toast.LENGTH_SHORT).show()
                                            }
                                    }
                                } else {
                                    // Gagal mendaftarkan pengguna
                                    Toast.makeText(this, "Failed to register user: ${it.exception?.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                            else {
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

    private fun startVerificationCheck(user: FirebaseUser) {
        verificationRunnable = object : Runnable {
            override fun run() {
                user.reload().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if (user.isEmailVerified) {
                            loginAutomatically(user.email!!, binding.edRegisterPassword.text.toString())
                        } else {
                            handler.postDelayed(this, 1500) // Cek ulang setiap 3 detik
                        }
                    } else {
                        Toast.makeText(this@RegistrasiActivity, "Failed to reload user.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        handler.post(verificationRunnable)
    }

    private fun loginAutomatically(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { loginTask ->
            if (loginTask.isSuccessful) {
                redirectToDashboard()
            } else {
                Toast.makeText(this, "Login failed: ${loginTask.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun redirectToDashboard() {
        val intent = Intent()
        intent.setClassName(this, "com.sampah.user.MainActivity")
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    // Tambahkan fungsi ini di luar `onCreate` atau di luar fungsi register
    private fun showVerificationFailedDialog(currentUser: FirebaseUser) {
        AlertDialog.Builder(this).apply {
            setTitle("Failed Verification")
            setMessage("Failed to send email verification")
            setPositiveButton("Send Again") { dialog, _ ->
                currentUser.sendEmailVerification()
                    .addOnCompleteListener { verificationTask ->
                        if (verificationTask.isSuccessful) {
                            Toast.makeText(this@RegistrasiActivity, "Email verification sent. Please check your email.", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@RegistrasiActivity, "Failed to send email verification: ${verificationTask.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
            setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            create()
            show()
        }
    }

    private fun setupAction() {
        binding.tvSignIn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}