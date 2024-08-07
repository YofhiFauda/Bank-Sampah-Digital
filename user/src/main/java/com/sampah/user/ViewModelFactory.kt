package com.sampah.user

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sampah.user.data.repository.DashboardRespository
import com.sampah.user.data.repository.ProfileRepository
import com.sampah.user.data.repository.TrashRepository
import com.sampah.user.di.Injection
import com.sampah.user.ui.dashboard.DashboardViewModel
import com.sampah.user.ui.profile.ProfileViewModel
import com.sampah.user.ui.trash.TrashViewModel

class ViewModelFactory private constructor(
    private val trashRepository: TrashRepository,
    private val dashboardRespository: DashboardRespository,
    private val profileRepository: ProfileRepository,
) : ViewModelProvider.NewInstanceFactory() {
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