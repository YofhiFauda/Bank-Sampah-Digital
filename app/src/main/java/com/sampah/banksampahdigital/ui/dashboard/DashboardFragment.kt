package com.sampah.banksampahdigital.ui.dashboard

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sampah.banksampahdigital.ViewModelFactory
import com.sampah.banksampahdigital.ui.onboarding.OnboardingActivity
import com.sampah.banksampahdigital.databinding.FragmentDashboardBinding
import com.sampah.banksampahdigital.utils.Utils.showToast
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale
import java.util.Timer
import java.util.TimerTask

@Suppress("DEPRECATION")
class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener
    private val viewModel: DashboardViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }

    private var autoScrollTimer: Timer? = null
    private val autoScrollDelay: Long = 5000 // Delay in milliseconds for auto-scroll
    private val REQUEST_CODE_INTERNET_PERMISSION = 1

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

        authStateListener = FirebaseAuth.AuthStateListener { auth ->
            val user = auth.currentUser
            if (user == null) {
                redirectToLoginScreen()
            } else {
                viewModel.loadUserData(user.uid)
                viewModel.loadTotalPengirimanDanPendapatan(user.uid)
            }
        }
        setListCarousel()
        setupView()
        // Check and request permissions
        checkAndRequestPermissions()
    }

    private fun setupView() {
        viewModel.user.observe(viewLifecycleOwner) { user ->
            Log.d("TAG", "Total Pendapatan: ${user.username}")
            binding?.tvNameUser?.text = "${user?.username}"
        }

        viewModel.totalPengiriman.observe(viewLifecycleOwner) { totalPengiriman ->
            Log.d("TAG", "Total Pendapatan: $totalPengiriman")
            binding?.tvTotalPengiriman?.text = "$totalPengiriman Pengiriman"
        }

        viewModel.totalPendapatan.observe(viewLifecycleOwner) { totalPendapatan ->
            val localeId = Locale("in", "ID")
            val numberFormat = NumberFormat.getNumberInstance(localeId)
            numberFormat.minimumFractionDigits = 0
            numberFormat.maximumFractionDigits = 0
            val formattedPendapatan = numberFormat.format(totalPendapatan).replace(",", ".")
            Log.d("TAG", "Total Pendapatan: $formattedPendapatan")
            binding?.tvTotalPendapatan?.text = "Rp $formattedPendapatan"
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
    private fun redirectToLoginScreen() {
        val intent = Intent(requireContext(), OnboardingActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        activity?.finish()
    }

    private fun checkAndRequestPermissions() {
        if (ContextCompat.checkSelfPermission( requireContext(), Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.INTERNET),
                REQUEST_CODE_INTERNET_PERMISSION
            )
        } else {
            // Permission already granted, proceed with the task
            val currentUser = firebaseAuth.currentUser
            if (currentUser != null) {
                viewModel.loadTotalPengirimanDanPendapatan(currentUser.uid)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_INTERNET_PERMISSION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission granted, proceed with the task
                    val currentUser = firebaseAuth.currentUser
                    if (currentUser != null) {
                        viewModel.loadTotalPengirimanDanPendapatan(currentUser.uid)
                    }
                } else {
                    // Permission denied, show a message to the user
                    showToast(requireActivity(), "Permission denied")
                }
                return
            }
        }
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
