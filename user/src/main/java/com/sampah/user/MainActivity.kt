package com.sampah.user

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sampah.user.databinding.ActivityMainBinding
import android.content.Intent
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener
    private lateinit var firestore: FirebaseFirestore
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.greenDark)

        setupNavController()
        setupBottomNavigationView()
        setupBroadcast()
    }

    private fun setupBroadcast() {
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            firestore.collection("users")
                .document(currentUser.uid)
                .collection("TrashSent")
                .get()
                .addOnSuccessListener { result ->
                    val data = result.documents

                    // Menyiarkan siaran ketika ada perubahan status
                    for (document in data) {
                        val status = document.getString("status")
                        val documentId = document.id
                        if (status != null && (status == "di terima" || status == "di tolak")) {
                            val intent = Intent("com.sampah.banksampahdigital.STATUS_CHANGED")
                            intent.putExtra("status", status)
                            intent.putExtra("documentId", documentId)
                            this.sendBroadcast(intent)
                        }
                    }
                }
                }
            }

    private fun setupNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        navController.addOnDestinationChangedListener { _, destination, _ ->
            setupBottomNavigationView()
        }
    }

    private fun setupBottomNavigationView() {
        bottomNavigationView = binding.bottomNavigation
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_dashboard -> {
                    navController.navigate(R.id.homeFragment)
                    true
                }
                R.id.action_sampah -> {
                    navController.navigate(R.id.trashFragment)
                    true
                }
                R.id.action_category -> {
                    navController.navigate(R.id.categoryFragment)
                    true
                }
                R.id.action_report -> {
                    navController.navigate(R.id.historyFragment)
                    true
                }
                R.id.action_setting -> {
                    navController.navigate(R.id.profileragment)
                    true
                }
                else -> false
            }
        }
    }
}