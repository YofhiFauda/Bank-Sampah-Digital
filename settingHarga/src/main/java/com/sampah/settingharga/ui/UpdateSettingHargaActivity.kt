package com.sampah.settingharga.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.sampah.settingharga.databinding.ActivityUpdateSettingHargaBinding

class UpdateSettingHargaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateSettingHargaBinding
    private lateinit var firestore: FirebaseFirestore

    private lateinit var jenisSampah: String
    private lateinit var hargaSampah: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateSettingHargaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mendapatkan data yang akan diedit dari intent
        jenisSampah = intent.getStringExtra("jenis_sampah") ?: ""
        hargaSampah = intent.getStringExtra("harga_sampah") ?: ""

        // Menampilkan jenis sampah yang akan diedit
        binding.tvJenisSampah.text = jenisSampah

        // Mengambil data harga sampah dari Firestore
        fetchHargaSampahFromFirestore()

        // Menyimpan perubahan saat tombol simpan diklik
        binding.btnSimpan.setOnClickListener {
            val newHargaSampah = binding.edHargaSampah.text.toString().trim()
            updateHargaSampahInFirestore(newHargaSampah)
        }
    }
    private fun fetchHargaSampahFromFirestore() {
        firestore.collection("setting_harga")
            .document(hargaSampah)
            .get()
            .addOnSuccessListener { document ->
                val hargaSampah = document.getString("harga_sampah")
                binding.edHargaSampah.setText(hargaSampah)
            }
            .addOnFailureListener { exception ->
                // Handle any errors
                Toast.makeText(this, "Silahkan coba lagi", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateHargaSampahInFirestore(newHargaSampah: String) {
        firestore.collection("setting_harga")
            .document(hargaSampah)
            .update("harga_sampah", newHargaSampah)
            .addOnSuccessListener {
                // Berhasil memperbarui data
                finish() // Kembali ke activity sebelumnya
            }
            .addOnFailureListener { exception ->
                // Handle any errors
                Toast.makeText(this, "Silahkan coba lagi", Toast.LENGTH_SHORT).show()
            }
    }
}