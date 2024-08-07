package com.sampah.admin

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.sampah.admin.data.model.MenuItem

class MenuAdapter(private val context: Context, private val menuList: List<MenuItem>) : BaseAdapter() {

    override fun getCount(): Int = menuList.size

    override fun getItem(position: Int): Any = menuList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        if (convertView == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.item_grid_menu, parent, false)
        } else {
            view = convertView
        }

        val imageView: ImageView = view.findViewById(R.id.itemIcon)
        val textView: TextView = view.findViewById(R.id.itemTitle)

        val menuKategori = menuList[position]
        imageView.setImageResource(menuKategori.icon)
        textView.text = menuKategori.title

        view.setOnClickListener {
            val intent = Intent(context, menuKategori.activityClass)
            context.startActivity(intent)
        }

        return view
    }
}

