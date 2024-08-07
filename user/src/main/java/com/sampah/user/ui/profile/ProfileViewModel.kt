package com.sampah.user.ui.profile

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sampah.user.data.model.User
import com.sampah.user.data.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: ProfileRepository) : ViewModel() {

    private val _userData = MutableLiveData<User>()
    val userData: LiveData<User> get() = _userData

    init {
        loadUserData()
    }
    private fun loadUserData() {
        repository.getUserData(
            onSuccess = { user ->
                _userData.value = user
            },
            onFailure = { exception ->
                Log.d(ContentValues.TAG, "Failed to load user data: ${exception.message}")
            }
        )
    }

    fun signOut() {
        repository.signOut()
    }

    fun saveImagePath(path: String) {
        viewModelScope.launch {
            repository.saveImagePath(path)
        }
    }

    fun getImagePath(): Flow<String?> {
        return repository.getImagePath()
    }
}