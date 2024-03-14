package com.sampah.core.data.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "sampah")
data class SampahEntity(
    @PrimaryKey
    val id: String,
    val namaPengguna: String,
    val kategoriSampah: String,
    val beratSampah: String,
    val hargaSampah: String,
    val tanggalPenjemputan: String,
    val alamatPenjemputan: String,
    val pendapatan: String,
    val status: String,
) :Parcelable