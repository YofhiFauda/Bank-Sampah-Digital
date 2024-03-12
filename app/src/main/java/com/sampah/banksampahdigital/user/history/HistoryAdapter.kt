package com.sampah.banksampahdigital.user.history

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.sampah.banksampahdigital.databinding.HistoryItemBinding
import java.text.SimpleDateFormat
import java.util.Locale

class HistoryAdapter (private val dataList: MutableList<DocumentSnapshot>) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: HistoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: DocumentSnapshot) {
            binding.apply {
                tvNamaPengguna.text = data.getString("Nama Pengguna")

                // Format tanggal penjemputan
                val dateString = data.getString("Tanggal Penjemputan") ?: ""
                val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
                val date = inputFormat.parse(dateString)
                val formattedDate = date?.let { outputFormat.format(it) }
                tvTanggalJemput.text = formattedDate

                //Kategory Sampah
                val jenisSampahString = data.getString("Kategori Sampah")
                val jenisSampah = String.format("Sampah $jenisSampahString")
                tvJenisSampah.text = jenisSampah

                //Berat Sampah
                val beratSampahString = data.getString("Berat Sampah")
                val beratSampah = String.format("$beratSampahString Kg")
                tvBeratSampah.text = beratSampah

                //Pendapatan
                val pendapatanString = data.getString("Pendapatan") ?: "0"
                val pendapatanDouble = pendapatanString.toDoubleOrNull() ?: 0.0
                val totalPendapatanFormat = String.format("Rp %,.3f", pendapatanDouble)
                tvTotalPendapatan.text = totalPendapatanFormat

                //Status
                tvStatus.text = data.getString("Status")
                // Atur warna teks berdasarkan status
                when(data.getString("Status")) {
                    "di proses" -> tvStatus.setTextColor(Color.RED)
                    "di tolak" -> tvStatus.setTextColor(Color.RED)
                    else -> tvStatus.setTextColor(Color.GREEN)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            HistoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}