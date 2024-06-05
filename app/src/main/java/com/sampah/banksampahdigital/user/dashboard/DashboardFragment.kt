package com.sampah.banksampahdigital.user.dashboard

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sampah.banksampahdigital.common.onboarding.OnboardingActivity
import com.sampah.banksampahdigital.databinding.FragmentDashboardBinding
import java.util.Timer
import java.util.TimerTask

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener
    private lateinit var firestore: FirebaseFirestore

    private var autoScrollTimer: Timer? = null
    private val autoScrollDelay: Long = 3000 // Delay in milliseconds for auto-scroll
    private var isUserScrolling = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
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
        setDashboard()
        setListCarousel()
        getTotalPengirimanDanPendapatan()
    }

    private fun getTotalPengirimanDanPendapatan() {
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            firestore.collection("users")
                .document(currentUser.uid)
                .collection("TrashSent")
                .get()
                .addOnSuccessListener { documents ->
                    var totalPengiriman = 0
                    var totalPendapatan = 0.0

                    for (document in documents) {
                        // Hitung total pengiriman
                        totalPengiriman++

                        // Ambil nilai pendapatan dari dokumen
                        val pendapatanField = document.getString("Pendapatan")
                        val statusField = document.getString("Status")

                        if (statusField == "di terima") {
                            // Konversi nilai pendapatan dari string ke double jika tidak null atau kosong
                            if (!pendapatanField.isNullOrEmpty()) {
                                try {
                                    val pendapatan = pendapatanField.toDouble()
                                    totalPendapatan += pendapatan
                                } catch (e: NumberFormatException) {
                                    Log.e("TAG", "Invalid Pendapatan value in document: ${document.id}")
                                    // Tambahkan penanganan kesalahan di sini jika diperlukan
                                }
                            } else {
                                Log.e("TAG", "Pendapatan field is empty or null in document: ${document.id}")
                                // Tambahkan penanganan kesalahan di sini jika diperlukan
                            }
                        } else {
                            Log.e("TAG", "Status field is not 'di terima' in document: ${document.id}")
                            totalPendapatan = 0.0
                        }
                    }


                    val totalPendapatanFormat = String.format("Rp %,.3f", totalPendapatan)
                    val totalPengirimanFormat = String.format("$totalPengiriman Pengiriman")

                    // Lakukan sesuatu dengan total pengiriman dan total pendapatan, misalnya tampilkan atau simpan
                    Log.d("TAG", "Total Pengiriman: $totalPengiriman")
                    binding?.tvTotalPengiriman?.text = totalPengirimanFormat


                    Log.d("TAG", "Total Pendapatan: $totalPendapatan")
                    binding?.tvTotalPendapatan?.text = totalPendapatanFormat
                }
                .addOnFailureListener { exception ->
                    Log.w("TAG", "Error getting documents: ", exception)
                }
        }
    }


    private fun setListCarousel() {
        // Inisialisasi RecyclerView
        val recyclerView = binding?.rvCarausel
        recyclerView?.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView?.adapter = CarouselAdapter(carouselItems)
        startAutoScroll()
    }

    private fun startAutoScroll() {
        autoScrollTimer = Timer()
        autoScrollTimer?.schedule(object : TimerTask() {
            override fun run() {
                requireActivity().runOnUiThread {
                    val layoutManager = binding?.rvCarausel?.layoutManager
                    if (layoutManager is LinearLayoutManager) {
                        val currentPosition = layoutManager.findFirstVisibleItemPosition()
                        val nextPosition =
                            if (currentPosition == carouselItems.size - 1) 0 else currentPosition + 1
                        binding?.rvCarausel?.smoothScrollToPosition(nextPosition)
                    } else {
                        Log.e("TAG", "Layout manager is not LinearLayoutManager")
                    }
                }
            }
        }, autoScrollDelay, autoScrollDelay)
    }


    private fun stopAutoScroll() {
        autoScrollTimer?.cancel()
        autoScrollTimer?.purge()
        autoScrollTimer = null
    }

    @SuppressLint("SetTextI18n")
    private fun setDashboard() {
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

                        binding?.tvNameUser?.text = "Username: $username"

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


    private fun redirectToLoginScreen() {
        val intent = Intent(requireContext(), OnboardingActivity::class.java)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        stopAutoScroll()
    }

    companion object {
        private val carouselItems = listOf(
            CarouselItem(
                "Selamat datang di\nAplikasi Bank Sampah!",
                "Mulailah Menabung Sampah Anda, dan Bersama-sama Kita Mengubah Dunia Menjadi Tempat yang Lebih Bersih",
                com.sampah.banksampahdigital.R.drawable.image_slider1
            ),
            CarouselItem(
                "Aksi Kecil\nDampak Besar!",
                "Setiap Aksi Kecil yang Anda Lakukan Hari Ini Akan Membawa Dampak yang Luar Biasa untuk Generasi Mendatang!",
                com.sampah.banksampahdigital.R.drawable.image_slider2
            ),
            CarouselItem(
                "Tukarkan Poin Anda\nDapatkan Manfaat Lebih",
                "Tukarkan Poin Anda dengan Barang Berguna atau Uang Tunai, Menjadi Reward untuk Kontribusi Anda terhadap Lingkungan!",
                com.sampah.banksampahdigital.R.drawable.image_slider3
            ),
            CarouselItem(
                "Sampah Bukan Akhir\nTapi Awal yang Berharga!",
                "Sampah Bukanlah Akhir dari Perjalanan, Tetapi Awal yang Berharga untuk Menciptakan Masa Depan yang Lebih Berkelanjutan!",
                com.sampah.banksampahdigital.R.drawable.image_slider4
            ),
            CarouselItem(
                "Tukarkan Sampah\nPlastikmu Sekarang!",
                "Aplikasi Bank Sampah adalah solusi untuk menyelesaikan masalah sosial tentang kebersihan lingkungan",
                com.sampah.banksampahdigital.R.drawable.image_slider5
            )
        )
    }
}
