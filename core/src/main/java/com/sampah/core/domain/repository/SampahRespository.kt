package com.sampah.core.domain.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.sampah.core.data.local.entity.SampahEntity
import com.sampah.core.data.local.room.SampahDao
import kotlinx.coroutines.Dispatchers
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.withContext

class SampahRespository private constructor(
    private val sampahDao: SampahDao,
    private val firestore: FirebaseFirestore
){

    fun getAllSampah(uid: String) {
            firestore.collection("users")
                .document(uid)
                .collection("TrashSent")
                .orderBy("Tanggal Penjemputan", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener { result ->
                    val data = result.documents

                    // Membagi data berdasarkan status
                    val inProcessData = data.filter { it.getString("Status") == "di proses" }
                    val acceptedData = data.filter { it.getString("Status") == "di terima" }

                    // Menggabungkan kembali data dengan urutan yang diinginkan
                    val sortedData = mutableListOf<DocumentSnapshot>()
                    sortedData.addAll(inProcessData)
                    sortedData.addAll(acceptedData)

                    val sampahList = sortedData.map {
                        SampahEntity(
                            id = it.id,
                            namaPengguna = it.getString("Nama Pengguna")?: "",
                            kategoriSampah = it.getString("Kategori Sampah")?:"",
                            beratSampah = it.getString("Berat Sampah")?: "",
                            hargaSampah= it.getString("Harga Sampah")?: "",
                            tanggalPenjemputan= it.getString("Tanggal Penjemputan")?:"",
                            alamatPenjemputan= it.getString("Alamat Penjemputan")?:"",
                            pendapatan= it.getString("Pendapatan")?:"",
                            status= it.getString("Status")?:"",
                        )

                    }
                    Log.d("Sampah class", "$sampahList")
                    sampahDao.insertSampah(sampahList)
                }
    }


//    fun getAllSampah(): Flow<List<SampahEntity>> {
//        return getAllSampah()
//    }


    companion object {
        @Volatile
        private var INSTANCE: SampahRespository? = null
        fun getInstance(
            sampahDao: SampahDao,
            firebaseFirestore: FirebaseFirestore,
        ): SampahRespository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: SampahRespository(sampahDao, firebaseFirestore)
            }.also { INSTANCE = it }
    }
}
