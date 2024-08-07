package com.sampah.user.ui.history

import android.app.AlertDialog
import android.content.ContentValues
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.sampah.user.databinding.HistoryItemBinding
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

class HistoryAdapter (private val dataList: MutableList<DocumentSnapshot>) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: HistoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.tvHapusSampah.setOnClickListener {
                showDeleteConfirmationDialog()
            }
        }

        private fun showDeleteConfirmationDialog() {
            val builder = AlertDialog.Builder(itemView.context)
            builder.setTitle("Konfirmasi")
            builder.setMessage("Apakah Anda yakin ingin menghapus dokumen ini?")
            builder.setPositiveButton("Ya") { dialog, _ ->
                val position = adapterPosition
                val documentSnapshot = dataList[position]
                deleteDocument(documentSnapshot.id)
                dialog.dismiss()
            }
            builder.setNegativeButton("Tidak") { dialog, _ ->
                dialog.dismiss()
            }
            builder.create().show()
        }

        private fun deleteDocument(documentId: String) {
            val firebaseAuth = FirebaseAuth.getInstance()
            val firestore = FirebaseFirestore.getInstance()
            val currentUser = firebaseAuth.currentUser
            if (currentUser != null) {
                firestore.collection("users")
                    .document(currentUser.uid)
                    .collection("TrashSent")
                    .document(documentId)
                    .delete()
                    .addOnSuccessListener {
                        Toast.makeText(itemView.context, "Dokumen berhasil dihapus", Toast.LENGTH_SHORT).show()
                        dataList.removeAt(bindingAdapterPosition)
                        notifyItemRemoved(bindingAdapterPosition)
                    }
                    .addOnFailureListener { e ->
                        Log.w(ContentValues.TAG, "Error deleting document", e)
                        Toast.makeText(itemView.context, "Gagal menghapus dokumen", Toast.LENGTH_SHORT).show()
                    }
            }
        }

        fun bind(data: DocumentSnapshot) {
            binding.apply {
                tvNamaPengguna.text = data.getString("namaPengguna")

                // Format tanggal penjemputan
                val dateString = data.getString("tanggalPenjemputan") ?: ""
                val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
                val date = inputFormat.parse(dateString)
                val formattedDate = date?.let { outputFormat.format(it) }
                tvTanggalJemput.text = formattedDate

                //Kategory Sampah
                val jenisSampahString = data.getString("kategoriSampah")
                val jenisSampah = String.format("Sampah $jenisSampahString")
                tvJenisSampah.text = jenisSampah

                //Berat Sampah
                val beratSampahString = data.getString("beratSampah")
                val beratSampah = String.format("$beratSampahString Kg")
                tvBeratSampah.text = beratSampah

                //Pendapatan
                val pendapatanString = data.getString("pendapatan") ?: "0"
                val pendapatanDouble = pendapatanString.toDoubleOrNull() ?: 0.0
                val formatedPendapatan = pendapatanDouble * 1000
                val localeId = Locale("in", "ID")
                val numberFormat = NumberFormat.getNumberInstance(localeId)
                numberFormat.minimumFractionDigits = 0
                numberFormat.maximumFractionDigits = 0
                val totalPendapatanFormat = numberFormat.format(formatedPendapatan).replace(",", ".")
                tvTotalPendapatan.text = "Rp $totalPendapatanFormat"

                //Status
                tvStatus.text = data.getString("status")
                // Atur warna teks berdasarkan status
                when(data.getString("status")) {
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