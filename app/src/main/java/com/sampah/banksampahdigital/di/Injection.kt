package com.sampah.banksampahdigital.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sampah.banksampahdigital.data.repository.TrashRepository

object Injection {
    fun provideTrashRepository(context: Context): TrashRepository {
        val firebaseAuth = FirebaseAuth.getInstance()
        val  firestore = FirebaseFirestore.getInstance()
        return TrashRepository.getInstance(firebaseAuth, firestore)
    }
}