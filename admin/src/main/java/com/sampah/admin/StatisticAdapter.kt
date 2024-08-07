package com.sampah.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.sampah.admin.data.model.Statistic

class StatisticAdapter(private val statistics: List<Statistic>) :
    RecyclerView.Adapter<StatisticAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: CardView = itemView.findViewById(R.id.statistic_cardview)
        val tvNumber: TextView = itemView.findViewById(R.id.tvNumber)
        val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_statistic, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val statistic = statistics[position]
        holder.tvNumber.text = statistic.number.toString()
        holder.tvDescription.text = statistic.description
        // Set the background color of the CardView dynamically
        holder.cardView.setCardBackgroundColor(holder.itemView.context.getColor(statistic.backgroundColor))
    }

    override fun getItemCount(): Int = statistics.size
}