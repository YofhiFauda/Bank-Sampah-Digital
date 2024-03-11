package com.sampah.settingharga.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.sampah.settingharga.R

class SettingHargaAdapter(private val dataSet: MutableList<DocumentSnapshot>) :
    RecyclerView.Adapter<SettingHargaAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvJenisSampah: TextView = view.findViewById(R.id.tv_jenisSampah)
        val tvHargaSampah: TextView = view.findViewById(R.id.tv_hargaSampah)
        val tvButtonSimpan: Button = view.findViewById(R.id.btn_simpan)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.setting_harga_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataSet[position]
        val jenisSampah = data.getString("jenis_sampah")
        val hargaSampah = data.getString("harga_sampah")

        holder.tvJenisSampah.text = jenisSampah
        holder.tvHargaSampah.text = hargaSampah
        holder.tvButtonSimpan.setOnClickListener {
            val intent = Intent(it.context, UpdateSettingHargaActivity::class.java)
            it.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}