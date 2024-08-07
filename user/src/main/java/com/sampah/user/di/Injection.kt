package com.sampah.user.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sampah.user.data.repository.DashboardRespository
import com.sampah.user.data.repository.ProfileRepository
import com.sampah.user.data.repository.TrashRepository
import com.sampah.user.datastore.PreferencesHelper
import com.sampah.user.datastore.dataStore


object Injection {
    fun providePreferencesHelper(context: Context): PreferencesHelper {
        return PreferencesHelper(context.dataStore)
    }
    fun provideTrashRepository(context: Context): TrashRepository {
        val firebaseAuth = FirebaseAuth.getInstance()
        val  firestore = FirebaseFirestore.getInstance()
        return TrashRepository.getInstance(firebaseAuth, firestore)
    }
    fun provideDashboardRepository(context: Context): DashboardRespository {
        val  firestore = FirebaseFirestore.getInstance()
        return DashboardRespository.getInstance( firestore)
    }
    fun provideProfileRepository(context: Context): ProfileRepository {
        val firebaseAuth = FirebaseAuth.getInstance()
        val firestore = FirebaseFirestore.getInstance()
        val preferencesHelper = providePreferencesHelper(context)
        return ProfileRepository.getInstance(firebaseAuth, firestore, preferencesHelper)
    }
}