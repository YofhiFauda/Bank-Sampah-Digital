package com.sampah.core.di

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.sampah.core.data.local.room.SampahDatabase
import com.sampah.core.domain.repository.SampahRespository
import com.sampah.core.utils.AppExecutors

object  SampahInjection {
    fun provideSampahRepository(context: Context): SampahRespository {
        val sampahDatabase = SampahDatabase.getDatabase(context)
        val sampahnDao = sampahDatabase.sampahDao()
        val firebaseFirestore = FirebaseFirestore.getInstance()
        return SampahRespository.getInstance(sampahnDao,firebaseFirestore)
    }
}