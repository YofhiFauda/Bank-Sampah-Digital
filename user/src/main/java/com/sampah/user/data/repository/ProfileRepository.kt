package com.sampah.user.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sampah.user.data.model.User
import com.sampah.user.datastore.PreferencesHelper
import kotlinx.coroutines.flow.Flow

class ProfileRepository(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val preferencesHelper: PreferencesHelper
) {
    fun getUserData(onSuccess: (User) -> Unit, onFailure: (Exception) -> Unit) {
        val userId = firebaseAuth.currentUser?.uid
        if (userId != null) {
            firestore.collection("users").document(userId)
                .get()
                .addOnSuccessListener { document ->
                    val user = document.toObject(User::class.java)
                    if (user != null) {
                        onSuccess(user)
                    } else {
                        onFailure(Exception("No such document"))
                    }
                }
                .addOnFailureListener { exception ->
                    onFailure(exception)
                }
        } else {
            onFailure(Exception("User not logged in"))
        }
    }

    fun signOut() {
        firebaseAuth.signOut()
    }

    suspend fun saveImagePath(path: String) {
        preferencesHelper.saveImagePath(path)
    }

    fun getImagePath(): Flow<String?> {
        return preferencesHelper.profileImagePath
    }

    companion object {
        @Volatile
        private var INSTANCE: ProfileRepository? = null
        fun getInstance(
            firebaseAuth: FirebaseAuth,
            firestore: FirebaseFirestore,
            preferencesHelper: PreferencesHelper
        ): ProfileRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: ProfileRepository(firebaseAuth, firestore, preferencesHelper)
            }.also { INSTANCE = it }
    }
}