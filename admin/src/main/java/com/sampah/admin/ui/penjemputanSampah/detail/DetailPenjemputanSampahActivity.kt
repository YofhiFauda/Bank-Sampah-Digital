package com.sampah.admin.ui.penjemputanSampah.detail

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.sampah.admin.R
import com.sampah.admin.data.model.Sampah
import com.sampah.admin.databinding.ActivityDetailPenjemputanSampahBinding
import com.sampah.admin.ui.penjemputanSampah.PenjemputanSampahActivity

class DetailPenjemputanSampahActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailPenjemputanSampahBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var documentId: String
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPenjemputanSampahBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi Firestore
        firestore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

        // Ambil data dari Intent
        val namaPengguna = intent.getStringExtra("namaPengguna")
        val kategoriSampah = intent.getStringExtra("kategoriSampah")
        val tanggalPenjemputan = intent.getStringExtra("tanggalPenjemputan")
        val beratSampah = intent.getStringExtra("beratSampah")
        val hargaSampah = intent.getStringExtra("hargaSampah")
        val alamatPenjemputan = intent.getStringExtra("alamatPenjemputan")
        val catatan = intent.getStringExtra("catatan")
        documentId = intent.getStringExtra("documentId") ?: ""
        userId = intent.getStringExtra("userId") ?: ""


        // Tampilkan data di TextViews atau UI lain
        binding.subtitleNamaPengguna.text = namaPengguna
        binding.subtitleKategoriSampah.text = kategoriSampah
        binding.subtitleTanggalPenjemputan.text = tanggalPenjemputan
        binding.subtitleBeratSampah.text = beratSampah
        binding.subtitleHargaSampah.text = hargaSampah
        binding.subtitleAlamatPenjemputan.text = alamatPenjemputan
        binding.subtitleCatatan.text = catatan

        loadStatusOptions()

        binding.btnBack.setOnClickListener {
            val intent = Intent(this, PenjemputanSampahActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        binding.btnSimpan.setOnClickListener {
            updateStatus()
        }
    }
    private fun loadStatusOptions() {
        firestore.collection("status")
            .get()
            .addOnSuccessListener { result ->
                val statusList = mutableListOf<String>()
                for (document in result) {
                    val status = document.getString("status")
                    if (status != null) {
                        statusList.add(status)
                    }
                }
                val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, statusList)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerStatus.adapter = adapter

            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "get failed with ", exception)
                Toast.makeText(this, "Failed to load status options", Toast.LENGTH_SHORT).show()
            }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateStatus() {
        val selectedStatus = binding.spinnerStatus.selectedItem.toString()
        firestore.collection("users")
            .document(userId)
            .collection("TrashSent")
            .document(documentId)
            .update("status", selectedStatus)
            .addOnSuccessListener { querySnapshot ->
                Log.d(ContentValues.TAG, "Document: $querySnapshot User id: $userId Document ID: $documentId")
                Toast.makeText(this, "Status updated successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "get failed with ", exception)
                Toast.makeText(this, "Gagal Memuat Data", Toast.LENGTH_SHORT).show()
            }
    }

//    @SuppressLint("NotifyDataSetChanged")
//    private fun updateStatus() {
//        val selectedStatus = binding.spinnerStatus.selectedItem.toString()
//        firestore.collection("users")
//            .get()
//            .addOnSuccessListener { querySnapshot ->
//                for (userDocument in querySnapshot.documents) {
//                    val userId = userDocument.id
//                    firestore.collection("users")
//                        .document(userId)
//                        .collection("TrashSent")
//                        .document(documentId)
//                        .update("status", selectedStatus)
//                        .addOnSuccessListener {
//                            Log.d(ContentValues.TAG, "Document: $userDocument User id: $userId Document ID: $documentId")
//                            Toast.makeText(this, "Status updated successfully", Toast.LENGTH_SHORT).show()
//                            finish()
//                        }
//                        .addOnFailureListener { exception ->
//                            Log.d(ContentValues.TAG, "get failed with ", exception)
//                            Toast.makeText(this, "Gagal Memperbarui Status", Toast.LENGTH_SHORT).show()
//                        }
//                }
//            }
//            .addOnFailureListener { exception ->
//                Log.d(ContentValues.TAG, "get failed with ", exception)
//                Toast.makeText(this, "Gagal Memuat Data", Toast.LENGTH_SHORT).show()
//            }
//    }

//    @SuppressLint("NotifyDataSetChanged")
//    private fun updateStatus() {
//        val selectedStatus = binding.spinnerStatus.selectedItem.toString()
//        firestore.collection("users")
//            .get()
//            .addOnSuccessListener { result ->
//                for (userDocument in result) {
//                    val userId = userDocument.id
//                    firestore.collection("users")
//                        .document(userId)
//                        .collection("TrashSent")
//                        .get()
//                        .addOnSuccessListener { trashResult ->
//                            for (trashDocument in trashResult){
//                                val trashDocumentId = trashDocument.id
//                                Log.d("DOCUMENT ID", trashDocumentId)
//                                firestore.collection("users")
//                                    .document(userId)
//                                    .collection("TrashSent")
//                                    .document(trashDocumentId)
//                                    .update("status", selectedStatus)
//                                    .addOnSuccessListener {
//                                        Toast.makeText(this, "Status updated successfully", Toast.LENGTH_SHORT).show()
//                                        finish()
//                                    }
//                                    .addOnFailureListener { exception ->
//                                        Log.d(ContentValues.TAG, "update failed with ", exception)
//                                        Toast.makeText(this, "Failed to update status", Toast.LENGTH_SHORT).show()
//                                    }
//                            }
//                        }
//                        .addOnFailureListener { exception ->
//                            Log.d(ContentValues.TAG, "get failed with ", exception)
//                            Toast.makeText(this, "User Tidak Ditemukan", Toast.LENGTH_SHORT).show()
//                        }
//                }
//            }
//            .addOnFailureListener { exception ->
//                Log.d(ContentValues.TAG, "get failed with ", exception)
//                Toast.makeText(this, "Gagal Memuat Data", Toast.LENGTH_SHORT).show()
//            }
//    }
}