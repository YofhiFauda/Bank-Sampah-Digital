package com.sampah.banksampahdigital.user.profile

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sampah.banksampahdigital.R
import com.sampah.banksampahdigital.databinding.FragmentProfileBinding
import java.util.Locale

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        setupView()
        setupExit()
    }

    private fun setupView() {
        val userId = firebaseAuth.currentUser

        if (userId != null) {
            // Mengambil data pengguna dari Firestore berdasarkan ID pengguna
            firestore.collection("users").document(userId.uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        // Dokumen ditemukan, ambil data pengguna
                        val username = document.getString("username")
                        val email = document.getString("email")

                        binding?.tvTitlePengguna?.text = username
                        binding?.tvEmail?.text = email

                        // Mendapatkan huruf pertama dari username
                        binding?.profileImage?.setImageResource(R.drawable.img_sample_avatar)

                        // Lakukan apa pun dengan data pengguna yang diperoleh
                        Log.d(ContentValues.TAG, "Username: $username, Email: $email")
                    } else {
                        Log.d(ContentValues.TAG, "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    // Gagal mengambil data pengguna
                    Log.d(ContentValues.TAG, "get failed with ", exception)
                }
        }
    }

    private fun setupExit() {
        binding?.layoutKeluar?.setOnClickListener {
            AlertDialog.Builder(requireActivity()).apply {
                setTitle("Peringatan!")
                setMessage("Apakah anda yakin ingin keluar?")
                setPositiveButton("Ya") { _, _ ->
                    firebaseAuth.signOut()
                    activity?.finish()
                }
                setNegativeButton("Tidak") { dialog, _ ->
                    dialog.dismiss()
                }
                create()
                show()
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}