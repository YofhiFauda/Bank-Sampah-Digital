package com.sampah.user.ui.profile.dataPribadi

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.sampah.user.databinding.ActivityDataPribadiBinding
import com.sampah.user.ui.profile.ProfileViewModel
import com.sampah.user.ViewModelFactory
import com.sampah.user.R
import kotlinx.coroutines.launch
import java.io.File

@Suppress("DEPRECATION")
class DataPribadiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDataPribadiBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private var isEdit = false
    private val viewModel: ProfileViewModel by viewModels{
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDataPribadiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.greenDark)


        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        loadDataUser()
        updateProfile()
        loadProfileImage()
        setupAction()
    }

    private fun loadProfileImage() {
        lifecycleScope.launch {
            viewModel.getImagePath().collect { path ->
                if (!path.isNullOrEmpty()) {
                    val file = File(filesDir, path)
                    if (file.exists()) {
                        val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                        binding?.profileImage?.setImageBitmap(bitmap)
                    }
                }
            }
        }
    }



    private fun loadDataUser() {
        val currentUser = firebaseAuth.currentUser

        if (currentUser != null) {
            firestore.collection("users").document(currentUser.uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        binding.edNamaPengguna.setText(document.getString("username"))
                        binding.edEmailPengguna.setText(document.getString("email"))
                        binding.edNoTeleponPengguna.setText(document.getString("noTelepon"))
                        binding.edAlamatPengguna.setText(document.getString("alamat"))
                        isEdit = true
                    } else {
                        isEdit = false
                    }
                }
        }
    }

    private fun updateProfile() {
        binding.btnUpdateDataPribadi.setOnClickListener {
            val namaPengguna = binding.edNamaPengguna.text.toString()
            val email = binding.edEmailPengguna.text.toString().trim()
            val noTelepon = binding.edNoTeleponPengguna.text.toString().trim()
            val alamat = binding.edAlamatPengguna.text.toString()

            val currentUser = firebaseAuth.currentUser

            val userUpdate = hashMapOf(
                "username" to namaPengguna,
                "email" to email,
                "noTelepon" to noTelepon,
                "alamat" to alamat
            ).filterValues { it.isNotEmpty() } // Hanya menyimpan nilai yang tidak kosong

            if (currentUser != null) {
                firestore.collection("users").document(currentUser.uid)
                    .set(userUpdate, SetOptions.merge())
                    .addOnSuccessListener {
                        Toast.makeText(this, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show()
                        binding.btnUpdateDataPribadi.isEnabled = true
                        isEdit = true
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Data gagal diperbarui", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Pengguna tidak terautentikasi", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun setupAction() {
        binding.topAppBar.setOnClickListener {
            onBackPressed()
        }
    }

}