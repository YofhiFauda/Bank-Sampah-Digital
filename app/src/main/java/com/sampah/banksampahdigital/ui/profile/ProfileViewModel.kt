package com.sampah.banksampahdigital.ui.profile

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sampah.banksampahdigital.data.model.User
import com.sampah.banksampahdigital.data.repository.ProfileRepository

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
}