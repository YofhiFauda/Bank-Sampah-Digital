package com.sampah.admin.ui.penjemputanSampah

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.sampah.admin.R
import com.sampah.admin.data.model.Sampah
import com.sampah.admin.ui.penjemputanSampah.detail.DetailPenjemputanSampahActivity

class PenjemputanSampahAdapter(
    private val jemputSampah: List<Sampah>,
    private val context: Context,
): RecyclerView.Adapter<PenjemputanSampahAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val namaPengguna: TextView = itemView.findViewById(R.id.tv_namaPengguna)
        val tanggalPenjemputan: TextView = itemView.findViewById(R.id.tv_tanggalPenjemputan)
        val jenisSampah: TextView = itemView.findViewById(R.id.tv_jenisSampah)
        val beratSampah: TextView = itemView.findViewById(R.id.tv_beratSampah)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_penjemputan_sampah, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sampah = jemputSampah[position]
        holder.namaPengguna.text = sampah.namaPengguna
        holder.tanggalPenjemputan.text = sampah.tanggalPenjemputan
        holder.jenisSampah.text = sampah.kategoriSampah
        holder.beratSampah.text = "Berat ${sampah.beratSampah} Kg"
        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailPenjemputanSampahActivity::class.java).apply {
                putExtra("namaPengguna", sampah.namaPengguna)
                putExtra("kategoriSampah", sampah.kategoriSampah)
                putExtra("tanggalPenjemputan", sampah.tanggalPenjemputan)
                putExtra("beratSampah", sampah.beratSampah)
                putExtra("status", sampah.status)
                putExtra("alamatPenjemputan", sampah.alamatPenjemputan)
                putExtra("catatan", sampah.catatan)
                putExtra("hargaSampah", sampah.hargaSampah)
                putExtra("documentId", sampah.id)
                putExtra("userId", sampah.userId)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = jemputSampah.size
}