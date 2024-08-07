package com.sampah.admin

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sampah.admin.data.model.MenuItem
import com.sampah.admin.data.model.Statistic
import com.sampah.admin.databinding.ActivityAdminBinding
import com.sampah.admin.ui.historySampah.HistorySampahActivity
import com.sampah.admin.ui.jabatan.JabatanActivity
import com.sampah.admin.ui.pekerja.PekerjaActivity
import com.sampah.admin.ui.penjemputanSampah.PenjemputanSampahActivity
import com.sampah.admin.ui.profile.ProfileActivity
import com.sampah.admin.ui.riwayatPenukaran.RiwayatPenukaranActivity
import com.sampah.admin.ui.settingHadiah.SettingHadiahActivity
import com.sampah.admin.ui.settingHarga.SettingHargaActivity

class AdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        authStateListener = FirebaseAuth.AuthStateListener { auth ->
            val user = auth.currentUser
            if (user == null) {
                redirectToLoginScreen()
                Log.e(user?.uid, "Success Login UID: ${user?.uid}")
            }
        }

        setupView()
        gridMenuKategory()
        setupStatistik()

//        binding.btnKeluar.setOnClickListener {
//            firebaseAuth.signOut()
//            val intent = Intent()
//            intent.setClassName(this, "com.sampah.banksampahdigital.ui.welcome.WelcomeActivity")
//            startActivity(intent)
//            finish()
//        }
    }


    private fun redirectToLoginScreen() {
        val intent = Intent()
        intent.setClassName(this, "com.sampah.banksampahdigital.ui.welcome.WelcomeActivity")
        startActivity(intent)
        finish()
    }

    private fun setupView() {
        val userId = firebaseAuth.currentUser

        if (userId != null){
            firestore.collection("admin")
                .document(userId.uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        // Dokumen ditemukan, ambil data pengguna
                        val username = document.getString("username")

                        binding.tvUsername.text = "$username"
                        binding.tvJabatan.text = "Staff Operator"

                        // Lakukan apa pun dengan data pengguna yang diperoleh
                        Log.d(ContentValues.TAG, "Username: $username")
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

    private fun setupStatistik() {
        binding.rvStatistik.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val statistics = listOf(
            Statistic(1020, "Total\nPengiriman", R.color.soft_blue),
            Statistic(1020, "Pengiriman\ndi terima", R.color.soft_green),
            Statistic(1020, "Pengiriman\ndi proses", R.color.soft_orange),
            Statistic(1020, "Pengiriman\ndi tolak", R.color.soft_red)
        )
        binding.rvStatistik.adapter = StatisticAdapter(statistics)
    }
    private fun gridMenuKategory() {
        val menuList = listOf(
            MenuItem(R.drawable.icon_penjemputan_sampah, "Penjemputan\nSampah", PenjemputanSampahActivity::class.java),
            MenuItem(R.drawable.icon_setting_harga, "Setting\nHarga", SettingHargaActivity::class.java),
            MenuItem(R.drawable.icon_pekerja, "Pekerja", PekerjaActivity::class.java),
            MenuItem(R.drawable.icon_profile, "Profile", ProfileActivity::class.java),
            MenuItem(R.drawable.icon_jabatan, "Jabatan", JabatanActivity::class.java),
            MenuItem(R.drawable.icon_setting_harga, "Setting\nHadiah", SettingHadiahActivity::class.java),
            MenuItem(R.drawable.icon_history_sampah, "History\nSampah", HistorySampahActivity::class.java),
            MenuItem(R.drawable.icon_riwayat_penukaran, "Riwayat\nPenukaran", RiwayatPenukaranActivity::class.java)
        )

        val adapter = MenuAdapter(this, menuList)
        binding.gridView.adapter = adapter
    }

}