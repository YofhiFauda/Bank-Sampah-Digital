package com.sampah.user.ui.trash

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sampah.user.databinding.FragmentTrashBinding
import com.sampah.user.ui.trash.TrashViewModel
import com.sampah.user.R
import com.sampah.user.data.model.JemputSampah
import com.sampah.user.ViewModelFactory
import com.sampah.user.utils.Utils.showToast
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController

class TrashFragment : Fragment() {
    private var _binding: FragmentTrashBinding? = null
    private val binding get() = _binding


    private val viewModel: TrashViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }

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

        viewModel.loadHarga()
        setupObservers()

        binding?.btnSimpan?.setOnClickListener {
            Log.d("Navigation", "Button Simpan ditekan")

            buatJemputSampah()
        }
        binding?.edTanggalPenjemputan?.setOnClickListener {
            showDatePicker()
        }
    }

    private fun setupObservers() {
        viewModel.hargaMap.observe(viewLifecycleOwner) { hargaMap ->
            this.hargaMap = hargaMap
            setupSpinner()
        }
        viewModel.isSaved.observe(viewLifecycleOwner) { success ->
            if (success) {
                showToast(requireActivity(), "Data berhasil ditambahkan")
                clearForm()
                navigateToSuccessFragment()
            } else {
                showToast(requireActivity(), "Gagal menambahkan data")
            }
        }
    }

    private fun navigateToSuccessFragment() {
        Log.d("Navigation", "Navigating to SuccessJemputSampahFragment")
        try {
            val action = TrashFragmentDirections.actionTrashFragmentToSuccessJemputSampahFragment()
            findNavController().navigate(action)
        } catch (e: IllegalArgumentException) {
            Log.e("Navigation", "Navigation action/destination not found: ${e.message}")
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
        val namaPengguna = binding?.edNamaPengguna?.text.toString()
        val kategoriSampah = binding?.spinnerKategoriSampah?.selectedItem.toString()
        val beratSampah = binding?.edBeratSampah?.text.toString()
        val tanggalPenjemputan = binding?.edTanggalPenjemputan?.text.toString()
        val alamatPenjemputan = binding?.edAlamatPenjemputan?.text.toString()
        val catatan = binding?.edCatatanTambahan?.text.toString()

        val selectedHarga = hargaMap[kategoriSampah]
        binding?.tvHargaSampah?.text = selectedHarga ?: "Harga tidak tersedia"

        if (namaPengguna.isEmpty() || kategoriSampah.isEmpty() || beratSampah.isEmpty() ||
            tanggalPenjemputan.isEmpty() || alamatPenjemputan.isEmpty() || selectedHarga.isNullOrEmpty()) {
            showToast(requireActivity(), "Semua field harus diisi")
            return
        }

        val beratSampahDouble = beratSampah.toDoubleOrNull()
        val hargaPerKgDouble = selectedHarga.toDoubleOrNull()

        if (beratSampahDouble == null || hargaPerKgDouble == null) {
            showToast(requireActivity(), "Berat sampah atau harga tidak valid")
            return
        }

        val totalHarga = beratSampahDouble * hargaPerKgDouble
        val formatedHarga = totalHarga * 1000
        val localeId = Locale("in", "ID")
        val numberFormat = NumberFormat.getNumberInstance(localeId)
        numberFormat.minimumFractionDigits = 0
        numberFormat.maximumFractionDigits = 0
        val totalHargaString = numberFormat.format(formatedHarga).replace(",", ".")

        val jemputSampah = JemputSampah(
            namaPengguna,
            kategoriSampah,
            beratSampah,
            selectedHarga,
            tanggalPenjemputan,
            alamatPenjemputan,
            catatan,
            status = "di proses",
            pendapatan = totalHargaString
        )

        Log.e("namaPengguna", "$namaPengguna")
        Log.e("kategoriSampah", "$kategoriSampah")
        Log.e("beratSampah", "$beratSampah")
        Log.e("selectedHarga", "$selectedHarga")
        Log.e("tanggalPenjemputan", "$tanggalPenjemputan")
        Log.e("alamatPenjemputan", "$alamatPenjemputan")
        Log.e("catatan", "$catatan")
        Log.e("pendapatan", "$totalHargaString")

        viewModel.saveJemputSampah(jemputSampah)
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

    override fun onResume() {
        super.onResume()
        Log.d("TrashFragment", "TrashFragment onResume called")
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}