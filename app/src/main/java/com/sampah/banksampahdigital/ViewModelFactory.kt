package com.sampah.banksampahdigital

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sampah.banksampahdigital.data.repository.DashboardRespository
import com.sampah.banksampahdigital.data.repository.ProfileRepository
import com.sampah.banksampahdigital.di.Injection
import com.sampah.banksampahdigital.data.repository.TrashRepository
import com.sampah.banksampahdigital.ui.dashboard.DashboardViewModel
import com.sampah.banksampahdigital.ui.profile.ProfileViewModel
import com.sampah.banksampahdigital.ui.trash.TrashViewModel

class ViewModelFactory private constructor(
    private val trashRepository: TrashRepository,
    private val dashboardRespository: DashboardRespository,
    private val profileRepository: ProfileRepository,
) :ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TrashViewModel::class.java)) {
            return TrashViewModel(trashRepository) as T
        }
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            return DashboardViewModel(dashboardRespository) as T
        }
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(profileRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    Injection.provideTrashRepository(context),
                    Injection.provideDashboardRepository(context),
                    Injection.provideProfileRepository(context)
                )
            }.also { instance = it }
    }
}