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

data class TrashSetting(
    val jenisSampah: String,
    val hargaSampah: String
)
data class historyTrash (
    val id: String = "",
    val namaPengguna: String = "",
    val kategoriSampah: String = "",
    val beratSampah: String = "",
    val hargaSampah: String = "",
    val tanggalPenjemputan: String = "",
    val status: String = "",
    val pendapatan: String = ""
)

data class TrashSent (
    val pendapatan: Double = 0.0,
    val status: String = ""
)

data class User (
    val uid: String = "",
    val username: String = "",
    val email: String = ""
)