package com.sampah.admin.ui.settingHarga

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.sampah.admin.R
import com.sampah.admin.data.model.SettingHargaModel
import com.sampah.admin.databinding.ActivityAddEditSettingHargaBinding

@Suppress("DEPRECATION")
class AddEditSettingHargaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditSettingHargaBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var title: String
    private var isEdit = false
    private var documentId: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditSettingHargaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()


        val settingHarga = intent.getParcelableExtra<SettingHargaModel>(EXTRA_HARGA)

        documentId = intent.getStringExtra(EXTRA_SETTING_HARGA_ID) // Retrieve the document ID from the intent


        if (settingHarga != null) {
            loadDataHargaSampah(settingHarga)
        } else {
            binding.btnDelete.visibility = View.GONE
            binding.titleAppBarEditDanTambahSampah.text = getString(R.string.add_setting_harga_sampah)
        }
//        loadDataHargaSampah()
        setupAddEditSettingHargaSampah()

        binding.btnBack.setOnClickListener {
            val intent = Intent(this, SettingHargaActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadDataHargaSampah(settingHarga: SettingHargaModel) {
        binding.edJenisSampah.setText(settingHarga.jenis_sampah)
        binding.edHargaSampah.setText(settingHarga.harga_sampah)
        binding.btnDelete.visibility = View.VISIBLE
        binding.titleAppBarEditDanTambahSampah.text = getString(R.string.edit_setting_harga_sampah)
        isEdit = true
//        binding.btnDelete.setOnClickListener {
//                firestore.collection("setting_harga")
//                    .document()
//                    .delete()
//                    .addOnSuccessListener {
//                        Toast.makeText(this, "Data berhasil dihapus", Toast.LENGTH_SHORT).show()
//                        finish()
//                    }
//                    .addOnFailureListener {
//                        Toast.makeText(this, "Data gagal dihapus", Toast.LENGTH_SHORT).show()
//                    }
//            }
        }

    private fun setupAddEditSettingHargaSampah() {
        binding.btnSimpan.setOnClickListener {
            val jenisSampah = binding.edJenisSampah.text.toString()
            val hargaSampah = binding.edHargaSampah.text.toString().trim()

            if (jenisSampah.isEmpty() || hargaSampah.isEmpty()) {
                Toast.makeText(this, "Harap mengisi semua kolom", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val hargaSampahUpdate = hashMapOf(
                "jenis_sampah" to jenisSampah,
                "harga_sampah" to hargaSampah
            )

            if (documentId != null) {
                firestore.collection("setting_harga").document(documentId!!)
                    .set(hargaSampahUpdate, SetOptions.merge())
                    .addOnSuccessListener {
                        Toast.makeText(this, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show()
                        Log.d(ContentValues.TAG, "Document Id Jenis Sampah: $documentId")
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Data gagal diperbarui", Toast.LENGTH_SHORT).show()
                    }
            } else {
                // Handle case when documentId is null (e.g., when adding new document)
                firestore.collection("setting_harga").add(hargaSampahUpdate)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Data gagal ditambahkan", Toast.LENGTH_SHORT).show()
                    }
            }
        }

        binding.btnDelete.setOnClickListener {
            if (documentId != null) {
                firestore.collection("setting_harga").document(documentId!!)
                    .delete()
                    .addOnSuccessListener {
                        Toast.makeText(this, "Data berhasil dihapus", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Data gagal dihapus", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Tidak dapat menghapus data. ID dokumen tidak ditemukan.", Toast.LENGTH_SHORT).show()
            }
        }
    }


//    private fun loadDataHargaSampah(){
//        firestore.collection("setting_harga ")
//            .document()
//            .get()
//            .addOnSuccessListener { document ->
//                if (document != null && document.exists()) {
//                    title = getString(R.string.edit_setting_harga_sampah)
//                    binding.edJenisSampah.setText(document.getString("jenis_sampah"))
//                    binding.edHargaSampah.setText(document.getString("harga_sampah"))
//                    binding.btnDelete.visibility = View.VISIBLE
//                    isEdit = true
//                    Log.d(ContentValues.TAG, "Document id: ${document.id}, Document Data: ${document.data}")
//                } else {
//                    title = getString(R.string.add_setting_harga_sampah)
//                    binding.btnDelete.visibility = View.GONE
//                    isEdit = false
//                    Log.d(ContentValues.TAG, "Document id: ${document.id}, Document Data: ${document.data}")
//                }
//                binding.titleAppBarEditDanTambahSampah.text = title
//            }
//    }


//    private fun setupAddEditSettingHargaSampah() {
//        binding.btnSimpan.setOnClickListener {
//            val jenisSampah = binding.edJenisSampah.text.toString()
//            val hargaSampah = binding.edHargaSampah.text.toString().trim()
//
//            val hargaSampahUpdate = hashMapOf(
//                "jenis_sampah" to jenisSampah,
//                "harga_sampah" to hargaSampah,
//            ).filterValues { it.isNotEmpty() } // Hanya menyimpan nilai yang tidak kosong
//
//            firestore.collection("setting_harga")
//                .document()
//                .set(hargaSampahUpdate, SetOptions.merge())
//                .addOnSuccessListener {
//                    Toast.makeText(this, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show()
//                    binding.btnSimpan.isEnabled = true
//                    isEdit = true
//                }
//                .addOnFailureListener {
//                    Toast.makeText(this, "Data gagal diperbarui", Toast.LENGTH_SHORT).show()
//                }
//
//        }
//    }

    companion object{
        const val EXTRA_HARGA = "extra_harga"
        const val EXTRA_SETTING_HARGA_ID = "extra_setting_harga_id"
    }
}