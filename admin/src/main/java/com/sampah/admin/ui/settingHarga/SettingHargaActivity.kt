package com.sampah.admin.ui.settingHarga

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.sampah.admin.AdminActivity
import com.sampah.admin.R
import com.sampah.admin.data.model.Sampah
import com.sampah.admin.data.model.SettingHargaModel
import com.sampah.admin.databinding.ActivitySettingHargaBinding
import com.sampah.admin.ui.penjemputanSampah.PenjemputanSampahAdapter

class SettingHargaActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingHargaBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var  adapter: SettingHargaAdapter
    private lateinit var settingHargaList: ArrayList<SettingHargaModel>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingHargaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()

        // Initialize the settingHargaList and adapter
        settingHargaList = arrayListOf()
        adapter = SettingHargaAdapter(settingHargaList)

        // Set up the RecyclerView with the adapter
        binding.rvSettingHarga.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = this@SettingHargaActivity.adapter
        }

        setupSettingHarga()

        binding.btnBack.setOnClickListener {
            val intent = Intent(this, AdminActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        binding.btnFloatingSettingHarga.setOnClickListener {
            val intent = Intent(this, AddEditSettingHargaActivity::class.java)
            startActivity(intent)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setupSettingHarga() {
        firestore.collection("setting_harga")
            .get()  // .get() fetches the whole collection; you don't need .document() here
            .addOnSuccessListener { documents ->
                settingHargaList.clear()  // Clear the list to avoid duplicates
                for (document in documents) {
                    val settingHarga = document.toObject(SettingHargaModel::class.java)
                    settingHargaList.add(settingHarga)
                    Log.d(ContentValues.TAG, "Document id: ${document.id}, Document Data: ${document.data}")
                }
                adapter.notifyDataSetChanged()  // Notify the adapter about the data change
            }
            .addOnFailureListener {
                // Handle the failure if needed
            }
    }
}