package com.sampah.user.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sampah.user.data.model.JemputSampah

class TrashRepository ( private val firebaseAuth: FirebaseAuth, private val firestore: FirebaseFirestore) {

    fun getSettingHarga(callback: (Map<String, String>) -> Unit ) {
        firestore.collection("setting_harga").get()
            .addOnSuccessListener { documents ->
                val dataMap = HashMap<String, String>()
                for (document in documents){
                    val jenisSampah = document.getString("jenis_sampah") ?: ""
                    val hargaSampah = document.getString("harga_sampah") ?: ""
                    dataMap[jenisSampah] = hargaSampah
                }
                callback(dataMap)
            }
            .addOnFailureListener { exception ->
                Log.w("TrashRespository", "Error getting documents: ", exception)
                callback(emptyMap())
            }
    }

    fun saveJemputSampah(jemputSampah: JemputSampah, callback: (Boolean) -> Unit){
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            firestore.collection("users")
                .document(currentUser.uid)
                .collection("TrashSent")
                .document()
                .set(jemputSampah)
                .addOnSuccessListener {
                    callback(true)
        }
                .addOnFailureListener { exception ->
                    Log.w("TrashRepository", "Error getting documents: ", exception)
                    callback(false)
                }
        }else {
            callback(false)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: TrashRepository? = null
        fun getInstance(
             firebaseAuth: FirebaseAuth,
             firestore: FirebaseFirestore
        ): TrashRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: TrashRepository(firebaseAuth, firestore)
            }.also { INSTANCE = it }
    }

}