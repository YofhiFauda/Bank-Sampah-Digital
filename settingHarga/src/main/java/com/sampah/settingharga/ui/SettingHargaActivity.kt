package com.sampah.settingharga.ui

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sampah.settingharga.databinding.ActivitySettingHargaBinding

class SettingHargaActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingHargaBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var adapter: SettingHargaAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingHargaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        adapter = SettingHargaAdapter(mutableListOf())

        binding.rvSettingHarga.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = adapter

        }
        setListSettingData()
        addSettingHarga()
        backButton()
    }

    private fun setListSettingData() {
        firestore.collection("setting_harga")
            .get()
            .addOnSuccessListener { documents ->
                val data = documents.documents
                adapter = SettingHargaAdapter(data)
                binding.rvSettingHarga.adapter = adapter
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "get failed with ", exception)
                Toast.makeText(this, "silahkan coba lagi", Toast.LENGTH_SHORT).show()
            }
    }

    private fun addSettingHarga(){
        binding.btnFab.setOnClickListener {
            val intent = Intent(this, AddSettingHargaActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun backButton(){
        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}