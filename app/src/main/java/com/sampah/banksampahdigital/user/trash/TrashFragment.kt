package com.sampah.banksampahdigital.user.trash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sampah.banksampahdigital.R
import com.sampah.banksampahdigital.databinding.FragmentDashboardBinding
import com.sampah.banksampahdigital.databinding.FragmentTrashBinding
import com.sampah.banksampahdigital.utils.Utils.showToast

class TrashFragment : Fragment() {
    private var _binding: FragmentTrashBinding? = null
    private val binding get() = _binding

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTrashBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        buatJemputSampah()
    }

    private fun buatJemputSampah() {
        binding?.btnSimpan?.setOnClickListener {
            val namaPengguna = binding?.tvTitleNamaPengguna?.text.toString()
            val kategoriSampah = binding?.spinnerKategoriSampah?.selectedItem.toString()
            val beratSampah = binding?.edBeratSampah?.text.toString()
            val tanggalPenjemputan = binding?.edTanggalPenjemputan?.text.toString()
            val alamatPenjemputan = binding?.edAlamatPenjemputan?.text.toString()
            val catatan = binding?.edCatatanTambahan?.text.toString()

            when {
                namaPengguna.isEmpty() -> {
                    binding?.edNamaPengguna?.error = "Mohon lengkapi nama anda dahulu"
                }
                kategoriSampah.isEmpty() -> {
                    showToast(requireActivity(), "Mohon pilih jenis sampah anda")
                }
                beratSampah.isEmpty() -> {
                    binding?.edBeratSampah?.error = "Mohon isi berat sampah anda"
                }
                tanggalPenjemputan.isEmpty() -> {
                    binding?.edTanggalPenjemputan?.error = "Mohon masukan tanggal penjemputan"
                }
                alamatPenjemputan.isEmpty() -> {
                    binding?.edAlamatPenjemputan?.error = "Mohon masukan alamat anda"
                }
                else -> {
                    if(namaPengguna.isNotEmpty() && kategoriSampah.isNotEmpty() && beratSampah.isNotEmpty() && tanggalPenjemputan.isNotEmpty() && alamatPenjemputan.isNotEmpty()){
                        val currentUser = firebaseAuth.currentUser

                        val jemputSampah = hashMapOf(
                            "Nama Pengguna" to namaPengguna,
                            "Kategori Sampah" to kategoriSampah,
                            "Berat Sampah" to beratSampah,
                            "Tanggal Penjemputan" to tanggalPenjemputan,
                            "Alamat Penjemputan" to alamatPenjemputan,
                            "Catatan" to catatan,
                        )
                    }
                    else {
                        showToast(requireActivity(), "Mohon lengkapi data di atas ")
                    }
                }
            }
        }
    }


}