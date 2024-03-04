package com.sampah.banksampahdigital

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sampah.banksampahdigital.common.onboarding.OnboardingActivity
import androidx.core.view.forEach
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sampah.banksampahdigital.databinding.ActivityMainBinding
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

        authStateListener = FirebaseAuth.AuthStateListener { auth ->
            val user = auth.currentUser
            if (user == null) {
                redirectToLoginScreen()
            }
        }
        setupNavController()
        setupBottomNavigationView()
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


    private fun redirectToLoginScreen() {
        val intent = Intent(this, OnboardingActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
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