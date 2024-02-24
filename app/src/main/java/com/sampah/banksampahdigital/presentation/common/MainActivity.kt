package com.sampah.banksampahdigital.presentation.common

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.forEach
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sampah.banksampahdigital.R
import com.sampah.banksampahdigital.databinding.ActivityMainBinding
import com.sampah.banksampahdigital.presentation.common.onboarding.OnboardingActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
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



    }
    private fun setupNavController() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        navController.addOnDestinationChangedListener { _, destination, _ ->
            updateBottomNavigationIcons(destination.id)
        }
    }

    private fun setupBottomNavigationView() {
        bottomNavigationView = binding.bottomNavigation
        bottomNavigationView.setupWithNavController(navController)
    }

    private fun updateBottomNavigationIcons(destinationId: Int) {
        if (!::bottomNavigationView.isInitialized) return

        val homeIcon = R.drawable.icon_home_enable
        val trashIcon = R.drawable.icon_jemput_enable
        val categoryIcon = R.drawable.icon_category_enable
        val historyIcon = R.drawable.icon_riwayat_enable
        val profileIcon = R.drawable.icon_person_enable

        val icons = mapOf(
            R.id.homeFragment to homeIcon,
            R.id.trashFragment to trashIcon,
            R.id.categoryFragment to categoryIcon,
            R.id.historyFragment to historyIcon,
            R.id.profileragment to profileIcon
        )

        bottomNavigationView.menu.forEach { menuItem ->
            val iconId = if (menuItem.itemId == destinationId) {
                icons[menuItem.itemId] ?: return@forEach
            } else {
                getDisabledIcon(menuItem.itemId)
            }
            menuItem.setIcon(iconId)
        }
    }

    private fun getDisabledIcon(itemId: Int): Int {
        return when (itemId) {
            R.id.homeFragment -> R.drawable.icon_home_disable
            R.id.trashFragment -> R.drawable.icon_jemput_disable
            R.id.categoryFragment -> R.drawable.icon_category_disable
            R.id.historyFragment -> R.drawable.icon_riwayat_disable
            R.id.profileragment -> R.drawable.icon_person_disable
            else -> throw IllegalArgumentException("Unknown itemId: $itemId")
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