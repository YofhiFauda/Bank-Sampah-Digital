package com.sampah.admin.ui.penjemputanSampah

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
import com.sampah.admin.AdminActivity
import com.sampah.admin.R
import com.sampah.admin.data.model.Sampah
import com.sampah.admin.databinding.ActivityPenjemputanSampahBinding
import com.sampah.admin.ui.penjemputanSampah.detail.DetailPenjemputanSampahActivity

class PenjemputanSampahActivity : AppCompatActivity() {

    private lateinit var binding:ActivityPenjemputanSampahBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var  adapter:PenjemputanSampahAdapter
    private lateinit var trashPickupList: ArrayList<Sampah>
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPenjemputanSampahBinding.inflate(layoutInflater)
        setContentView(binding.root)


        firestore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        recyclerView = findViewById(R.id.rv_jemputSampah)
        recyclerView.layoutManager = LinearLayoutManager(this)
        trashPickupList = arrayListOf()
        adapter = PenjemputanSampahAdapter(trashPickupList, this)
        recyclerView.adapter = adapter


        setupJemputSampah()

        binding.topAppBar.setOnClickListener {
            val intent = Intent(this, DetailPenjemputanSampahActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setupJemputSampah() {
        firestore.collection("users")
            .get()
            .addOnSuccessListener { querySnapshot ->
                trashPickupList.clear()
                for (userDocument in querySnapshot.documents) {
                    val userId = userDocument.id
                    Log.d(ContentValues.TAG, "UserId: $userId")
                    firestore.collection("users")
                        .document(userId)
                        .collection("TrashSent")
                        .whereEqualTo("status", "di proses")
                        .get()
                        .addOnSuccessListener { document ->
                            for (documents in document) {
                                val trashPickUp = documents.toObject(Sampah::class.java)
                                trashPickUp.id = documents.id
                                trashPickUp.userId = userId
                                trashPickupList.add(trashPickUp)
                                Log.d(ContentValues.TAG, "UserId: $userId, TrashSentId: $trashPickUp.id")
                                Log.d(ContentValues.TAG, "Document: $userDocument Document id: $userId")
                            }
                            adapter.notifyDataSetChanged()
                        }
                        .addOnFailureListener { exception ->
                            Log.d(ContentValues.TAG, "get failed with ", exception)
                            Toast.makeText(this, "Silahkan coba lagi", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "get failed with ", exception)
                Toast.makeText(this, "Silahkan coba lagi", Toast.LENGTH_SHORT).show()
            }
    }
}