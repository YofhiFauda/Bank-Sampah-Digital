package com.sampah.user.utils

import android.content.Context
import android.widget.Toast

object Utils {
    fun showToast(context: Context, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}