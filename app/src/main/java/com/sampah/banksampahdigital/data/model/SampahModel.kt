package com.sampah.banksampahdigital.data.model

data class JemputSampah (
    val namaPengguna: String = "",
    val kategoriSampah: String = "",
    val beratSampah: String = "",
    val hargaSampah: String? = "",
    val tanggalPenjemputan: String = "",
    val alamatPenjemputan: String = "",
    val catatan: String = "",
    val status: String = "",
    val pendapatan: String = ""
)

data class User (
    val username: String = "",
    val email: String = ""
)