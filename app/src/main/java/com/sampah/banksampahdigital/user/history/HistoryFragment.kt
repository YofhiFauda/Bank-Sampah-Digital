package com.sampah.banksampahdigital.user.history

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.sampah.banksampahdigital.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var adapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding?.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        adapter = HistoryAdapter(mutableListOf())
        binding?.rvHistory?.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = adapter
        }

        setListHistory()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setListHistory() {
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            firestore.collection("users")
                .document(currentUser.uid)
                .collection("TrashSent")
                .orderBy("Tanggal Penjemputan", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener { result ->
                    val data = result.documents

                    // Membagi data berdasarkan status
                    val inProcessData = data.filter { it.getString("Status") == "di proses" }
                    val acceptedData = data.filter { it.getString("Status") == "di terima" }

                    // Menggabungkan kembali data dengan urutan yang diinginkan
                    val sortedData = mutableListOf<DocumentSnapshot>()
                    sortedData.addAll(inProcessData)
                    sortedData.addAll(acceptedData)

                    // Mengupdate adapter dengan data yang diurutkan
                    adapter = HistoryAdapter(sortedData)

                    binding?.rvHistory?.adapter = adapter
                }
                .addOnFailureListener { exception ->
                    Log.d(ContentValues.TAG, "get failed with ", exception)
                    Toast.makeText(requireActivity(), "silahkan coba lagi", Toast.LENGTH_SHORT).show()
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}