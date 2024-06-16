package com.sampah.banksampahdigital.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sampah.banksampahdigital.data.repository.DashboardRespository
import com.sampah.banksampahdigital.data.repository.ProfileRepository
import com.sampah.banksampahdigital.data.repository.TrashRepository

object Injection {
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
        val  firestore = FirebaseFirestore.getInstance()
        return ProfileRepository.getInstance(firebaseAuth, firestore)
    }
}