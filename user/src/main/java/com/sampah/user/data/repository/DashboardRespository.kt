package com.sampah.user.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.sampah.user.data.model.User

class DashboardRespository( private val firestore: FirebaseFirestore) {
    fun getUser(uid: String, callback: (User?) -> Unit) {
        firestore.collection("users").document(uid).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val user = User(
                        username = document.getString("username") ?: "",
                        email = document.getString("email") ?: ""
                    )
                    callback(user)
                } else {
                    callback(null)
                }
            }
            .addOnFailureListener {
                callback(null)
            }
    }

    fun getTotalPengirimanDanPendapatan(uid: String, callback: (Int, Double) -> Unit) {
        firestore.collection("users").document(uid).collection("TrashSent").get(Source.SERVER)
            .addOnSuccessListener { documents ->
                var totalPengiriman = 0
                var totalPendapatan = 0.0

                for (document in documents) {
                    totalPengiriman++

                    val pendapatanField = document.getString("pendapatan")
                    val statusField = document.getString("status")

                    if (statusField == "di terima" && !pendapatanField.isNullOrEmpty()) {
                        try {
                            val pendapatan = pendapatanField.toDouble()
                            totalPendapatan += pendapatan * 1000
                        } catch (e: NumberFormatException) {
                            Log.e("TAG", "Invalid Pendapatan value in document: ${document.id}")
                        }
                    }
                }
                callback(totalPengiriman, totalPendapatan)
            }
            .addOnFailureListener {
                callback(0, 0.0)
            }
    }

    companion object {
        @Volatile
        private var INSTANCE: DashboardRespository? = null
        fun getInstance(
            firestore: FirebaseFirestore
        ): DashboardRespository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: DashboardRespository(firestore)
            }.also { INSTANCE = it }
    }

}