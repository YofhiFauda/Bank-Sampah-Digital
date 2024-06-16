package com.sampah.banksampahdigital.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sampah.banksampahdigital.data.model.User

class ProfileRepository(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
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

    companion object {
        @Volatile
        private var INSTANCE: ProfileRepository? = null
        fun getInstance(
            firebaseAuth: FirebaseAuth,
            firestore: FirebaseFirestore,
        ): ProfileRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: ProfileRepository(firebaseAuth, firestore)
            }.also { INSTANCE = it }
    }

}