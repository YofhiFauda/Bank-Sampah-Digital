package com.sampah.banksampahdigital.ui.trash

import org.junit.Assert.*
import org.junit.Test

class TrashFragmentTest {

    @Test
    fun testCreateJemputSampah() {
        val fragment = TrashFragment()

        val namaPengguna = "Yopi"
        val kategoriSampah = "Kayu"
        val beratSampah = "12"
        val selectedHarga = "3000"
        val tanggalPenjemputan = "10/06/2024"
        val alamatPenjemputan = "Siman"
        val catatan = "Sudah Di Depan"

        val expected = hashMapOf(
            "Nama Pengguna" to namaPengguna,
            "Kategori Sampah" to kategoriSampah,
            "Berat Sampah" to beratSampah,
            "Harga Sampah" to selectedHarga,
            "Tanggal Penjemputan" to tanggalPenjemputan,
            "Alamat Penjemputan" to alamatPenjemputan,
            "Catatan" to catatan,
            "Status" to "di proses",
            "Pendapatan" to "36000.000"
        )

        val result = fragment.createJemputSampah(
            namaPengguna, kategoriSampah, beratSampah, selectedHarga,
            tanggalPenjemputan, alamatPenjemputan, catatan
        )

        assertNotNull(result)
        assertEquals(expected, result)
    }

    @Test
    fun testCreateJemputSampahIncompleteData() {
        val fragment = TrashFragment()

        val namaPengguna = "Yopi"
        val kategoriSampah = ""
        val beratSampah = "12"
        val selectedHarga = "3000"
        val tanggalPenjemputan = "10/06/2024"
        val alamatPenjemputan = "Siman"
        val catatan = "Sudah Di Depan"

        val result = fragment.createJemputSampah(
            namaPengguna, kategoriSampah, beratSampah, selectedHarga,
            tanggalPenjemputan, alamatPenjemputan, catatan
        )

        assertNull(result)
    }

    @Test
    fun testCreateJemputSampahInvalidWeight() {
        val fragment = TrashFragment()

        val namaPengguna = "Yopi"
        val kategoriSampah = "Kayu"
        val beratSampah = "invalid"
        val selectedHarga = "3000"
        val tanggalPenjemputan = "10/06/2024"
        val alamatPenjemputan = "Siman"
        val catatan = "Sudah Di Depan"

        val result = fragment.createJemputSampah(
            namaPengguna, kategoriSampah, beratSampah, selectedHarga,
            tanggalPenjemputan, alamatPenjemputan, catatan
        )

        assertNull(result)
    }

    @Test
    fun testCreateJemputSampahInvalidPrice() {
        val fragment = TrashFragment()

        val namaPengguna = "Yopi"
        val kategoriSampah = "Kayu"
        val beratSampah = "12"
        val selectedHarga = "invalid"
        val tanggalPenjemputan = "10/06/2024"
        val alamatPenjemputan = "Siman"
        val catatan = "Sudah Di Depan"

        val result = fragment.createJemputSampah(
            namaPengguna, kategoriSampah, beratSampah, selectedHarga,
            tanggalPenjemputan, alamatPenjemputan, catatan
        )

        assertNull(result)
    }
}
