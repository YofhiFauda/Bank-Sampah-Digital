package com.sampah.banksampahdigital.user.trash

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sampah.banksampahdigital.databinding.FragmentTrashBinding
import com.sampah.banksampahdigital.utils.Utils.showToast
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TrashFragment : Fragment() {
    private var _binding: FragmentTrashBinding? = null
    private val binding get() = _binding

    lateinit var firebaseAuth: FirebaseAuth
    lateinit var firestore: FirebaseFirestore
    var hargaMap: Map<String, String> = HashMap()

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
        getSettingHarga()
        binding?.edTanggalPenjemputan?.setOnClickListener {
            showDatePicker()
        }
    }

    private fun getSettingHarga() {
        firestore.collection("setting_harga").get()
            .addOnSuccessListener { documents ->
                val dataMap = HashMap<String, String>()
                for (document in documents) {
                    val jenisSampah = document.getString("jenis_sampah") ?: ""
                    val hargaSampah = document.getString("harga_sampah") ?: ""
                    dataMap[jenisSampah] = hargaSampah
                }
                hargaMap = dataMap
                setupSpinner()
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents: ", exception)
            }
    }

    private fun setupSpinner() {
        val spinner = binding?.spinnerKategoriSampah
        val jenisSampahList = ArrayList<String>(hargaMap.keys)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, jenisSampahList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner?.adapter = adapter

        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedJenisSampah = parent?.getItemAtPosition(position).toString()
                val selectedHarga = hargaMap[selectedJenisSampah]
                binding?.tvContohHargaSampah?.text = selectedHarga ?: "Harga tidak tersedia"
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun buatJemputSampah() {
        binding?.btnSimpan?.setOnClickListener {
            val namaPengguna = binding?.edNamaPengguna?.text.toString()
            val kategoriSampah = binding?.spinnerKategoriSampah?.selectedItem.toString()
            val beratSampah = binding?.edBeratSampah?.text.toString()
            val tanggalPenjemputan = binding?.edTanggalPenjemputan?.text.toString()
            val alamatPenjemputan = binding?.edAlamatPenjemputan?.text.toString()
            val catatan = binding?.edCatatanTambahan?.text.toString()

            val selectedHarga = hargaMap[kategoriSampah]
            binding?.tvHargaSampah?.text = selectedHarga ?: "Harga tidak tersedia"

            val jemputSampah = createJemputSampah(
                namaPengguna, kategoriSampah, beratSampah, selectedHarga,
                tanggalPenjemputan, alamatPenjemputan, catatan
            )

            if (jemputSampah != null) {
                saveJemputSampah(jemputSampah)
            }
        }
    }

    fun createJemputSampah(
        namaPengguna: String,
        kategoriSampah: String,
        beratSampah: String,
        selectedHarga: String?,
        tanggalPenjemputan: String,
        alamatPenjemputan: String,
        catatan: String
    ): Map<String, String>? {
        if (namaPengguna.isEmpty() || kategoriSampah.isEmpty() || beratSampah.isEmpty() ||
            tanggalPenjemputan.isEmpty() || alamatPenjemputan.isEmpty()) {
            return null
        }

        val beratSampahDouble = beratSampah.toDoubleOrNull() ?: return null
        val hargaPerKgDouble = selectedHarga?.toDoubleOrNull() ?: return null

        val totalHarga = beratSampahDouble * hargaPerKgDouble
        val totalHargaString = String.format("%.3f", totalHarga)

        return hashMapOf(
            "Nama Pengguna" to namaPengguna,
            "Kategori Sampah" to kategoriSampah,
            "Berat Sampah" to beratSampah,
            "Harga Sampah" to selectedHarga,
            "Tanggal Penjemputan" to tanggalPenjemputan,
            "Alamat Penjemputan" to alamatPenjemputan,
            "Catatan" to catatan,
            "Status" to "di proses",
            "Pendapatan" to totalHargaString
        )
    }

    private fun saveJemputSampah(jemputSampah: Map<String, String>) {
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            firestore.collection("users")
                .document(currentUser.uid)
                .collection("TrashSent")
                .document()
                .set(jemputSampah)
                .addOnSuccessListener {
                    Log.d("TAG", "DocumentSnapshot added with : $it")
                    showToast(requireActivity(), "Data berhasil ditambahkan")
                    clearForm()
                }
                .addOnFailureListener { e ->
                    Log.w("TAG", "Error adding document", e)
                    showToast(requireActivity(), "Gagal menambahkan data")
                }
        }
    }

    private fun clearForm() {
        binding?.edNamaPengguna?.text = null
        binding?.spinnerKategoriSampah?.setSelection(0)
        binding?.edBeratSampah?.text = null
        binding?.edTanggalPenjemputan?.text = null
        binding?.edAlamatPenjemputan?.text = null
        binding?.edCatatanTambahan?.text = null
        binding?.tvHargaSampah?.text = null
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { _, year, monthOfYear, dayOfMonth ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(Calendar.YEAR, year)
            selectedDate.set(Calendar.MONTH, monthOfYear)
            selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = dateFormat.format(selectedDate.time)
            binding?.edTanggalPenjemputan?.setText(date)
        }, year, month, dayOfMonth)

        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
