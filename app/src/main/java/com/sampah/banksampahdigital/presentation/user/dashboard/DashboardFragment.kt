package com.sampah.banksampahdigital.presentation.user.dashboard

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sampah.banksampahdigital.databinding.FragmentDashboardBinding
import com.sampah.banksampahdigital.presentation.common.login.LoginActivity
import com.sampah.banksampahdigital.presentation.common.onboarding.OnboardingActivity


class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener
    private lateinit var firestore: FirebaseFirestore
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        authStateListener = FirebaseAuth.AuthStateListener { auth ->
            val user = auth.currentUser
            if (user == null) {
                redirectToLoginScreen()
            }
        }

        setListTransaction()
    }


    @SuppressLint("SetTextI18n")
    private fun setListTransaction() {
        val userId  = firebaseAuth.currentUser

        if (userId != null) {
            // Mengambil data pengguna dari Firestore berdasarkan ID pengguna
            firestore.collection("users").document(userId.uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        // Dokumen ditemukan, ambil data pengguna
                        val username = document.getString("username")
                        val email = document.getString("email")

                        binding?.tvUidUser?.text = "UID: ${userId.uid}"
                        binding?.tvNameUser?.text = "Username: $username"
                        binding?.tvEmailUser?.text = "Email: $email"


                        // Lakukan apa pun dengan data pengguna yang diperoleh
                        Log.d(TAG, "Username: $username, Email: $email")
                    } else {
                        Log.d(TAG, "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    // Gagal mengambil data pengguna
                    Log.d(TAG, "get failed with ", exception)
                }
        }

        binding?.signoutButton?.setOnClickListener {
            firebaseAuth.signOut()
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }

    private fun redirectToLoginScreen() {
        val intent = Intent(activity, OnboardingActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        activity?.finish()
    }

    override fun onStart() {
        super.onStart()
        firebaseAuth.addAuthStateListener(authStateListener)
    }

    override fun onStop() {
        super.onStop()
        firebaseAuth.removeAuthStateListener(authStateListener)
    }

}