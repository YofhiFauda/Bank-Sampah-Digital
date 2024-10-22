package com.sampah.admin.ui.settingHarga

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sampah.admin.R
import com.sampah.admin.data.model.SettingHargaModel
import com.sampah.admin.ui.penjemputanSampah.detail.DetailPenjemputanSampahActivity

class SettingHargaAdapter(
    private val settingHarga: List<SettingHargaModel>,
): RecyclerView.Adapter<SettingHargaAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val subtitleJenisSampah: TextView = itemView.findViewById(R.id.subtitle_jenisSampah)
        val subtitleHargaSampah: TextView = itemView.findViewById(R.id.subtitle_hargaSampah)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_setting_harga, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val settingHarga = settingHarga[position]
        holder.subtitleJenisSampah.text = settingHarga.jenis_sampah
        holder.subtitleHargaSampah.text = "Rp ${settingHarga.harga_sampah}"
        holder.itemView.setOnClickListener {
            holder.itemView.setOnClickListener {
                val intent = Intent(it.context, AddEditSettingHargaActivity::class.java).apply {
                    putExtra("harga_sampah", settingHarga.harga_sampah)
                    putExtra("jenis_sampah", settingHarga.jenis_sampah)
                    putExtra("settingHargaId", settingHarga.settingHargaId)
                    putExtra("sampahId", settingHarga.sampahId)
                }
                it.context.startActivity(intent)
            }
        }
    }

//    @SuppressLint("SetTextI18n")
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val settingHarga = settingHarga[position]
//        holder.subtitleJenisSampah.text = settingHarga.jenis_sampah
//        holder.subtitleHargaSampah.text = "Rp ${settingHarga.harga_sampah}"
//        holder.itemView.setOnClickListener {
//            holder.itemView.setOnClickListener {
//                val intent = Intent(it.context, AddEditSettingHargaActivity::class.java)
//                intent.putExtra(AddEditSettingHargaActivity.EXTRA_HARGA, settingHarga)
//                intent.putExtra(AddEditSettingHargaActivity.EXTRA_SETTING_HARGA_ID, settingHarga.sampahId) // Pass the document ID
//                it.context.startActivity(intent)
//            }
//        }
//    }

    override fun getItemCount() = settingHarga.size
}