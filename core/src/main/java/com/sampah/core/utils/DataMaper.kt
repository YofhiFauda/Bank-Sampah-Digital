package com.sampah.core.utils

import com.google.firebase.firestore.DocumentSnapshot
import com.sampah.core.data.local.entity.SampahEntity

object DataMaper {
    fun mapFirestoreToEntities(document: DocumentSnapshot): SampahEntity {
        val id = document.id
        val namaPengguna = document.getString("Nama Pengguna")?: ""
        val kategoriSampah = document.getString("Kategori Sampah")?:""
        val beratSampah = document.getString("Berat Sampah")?: ""
        val hargaSampah= document.getString("Harga Sampah")?: ""
        val tanggalPenjemputan= document.getString("Tanggal Penjemputan")?:""
        val alamatPenjemputan= document.getString("Alamat Penjemputan")?:""
        val pendapatan= document.getString("Pendapatan")?:""
        val status= document.getString("Status")?:""

        return SampahEntity(
            id,namaPengguna, kategoriSampah, beratSampah, hargaSampah, tanggalPenjemputan, alamatPenjemputan, pendapatan, status
        )
    }
}