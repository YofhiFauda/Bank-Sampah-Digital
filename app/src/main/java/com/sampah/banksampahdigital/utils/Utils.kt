package com.sampah.banksampahdigital.utils

import android.content.Context
import android.widget.Toast

object Utils {
    fun toast(context: Context, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}