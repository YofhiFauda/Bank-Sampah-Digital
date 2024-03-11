package com.sampah.settingharga.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sampah.settingharga.R
import com.sampah.settingharga.databinding.ActivityAddSettingHargaBinding
import com.sampah.settingharga.databinding.ActivitySettingHargaBinding

class AddSettingHargaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddSettingHargaBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var adapter: SettingHargaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddSettingHargaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        settingHarga()
    }

    private fun settingHarga() {
        binding.btnSimpan.setOnClickListener {
            val jenisSampah = binding.edJenisSampah.text.toString()
            val hargaSampah = binding.edHargaSampah.text.toString()

            when {
                jenisSampah.isEmpty() -> {
                    binding.edJenisSampah.error = "Mohon isi jenis sampah dahulu"
                }
                hargaSampah.isEmpty() -> {
                    binding.edHargaSampah.error = "Mohon isi harga sampah dahulu"
                }

                else -> {
                    if (jenisSampah.isNotEmpty() && hargaSampah.isNotEmpty()){
                        val sampah = hashMapOf(
                            "jenis_sampah" to jenisSampah,
                            "harga_sampah" to hargaSampah
                        )

                        firestore.collection("setting_harga")
                            .add(sampah)
                            .addOnSuccessListener {
                                // Berhasil menambahkan data baru
                                finish() // Kembali ke activity sebelumnya
                            }
                            .addOnFailureListener { exception ->
                                Toast.makeText(this, "Silahkan coba lagi", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
            }
        }
    }
}