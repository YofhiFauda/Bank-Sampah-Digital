package com.sampah.banksampahdigital.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.sampah.banksampahdigital.R

class StatusChangeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        // Memeriksa apakah intent tidak null dan apakah ada data yang dikirimkan
        if (intent != null && intent.hasExtra("status")) {
            val status = intent.getStringExtra("status")
            val documentId = intent.getStringExtra("documentId")

            if (status != null && (status == "di terima" || status == "di tolak")) {
                // Mengirim notifikasi
                sendNotification(context, "Status Diubah", "Permintaan Anda telah $status")
                // Menandai bahwa notifikasi sudah ditampilkan
                saveNotificationStatus(context, documentId)
                Log.e(TAG, "onReceive: $status")
            }
        }
    }

    private fun sendNotification(context: Context?, title: String, message: String) {
        val notificationManager = context?.let {
            ContextCompat.getSystemService(
                it,
                NotificationManager::class.java
            )
        } as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("default", "Channel name", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, "default")
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_notifications_24)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notificationManager.notify(1, notification)
    }

    private fun saveNotificationStatus(context: Context?, documentId: String?) {
        // Menyimpan status notifikasi ke SharedPreferences
        val sharedPreferences = context?.getSharedPreferences("NotificationPref", Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.putBoolean(documentId, true)
        editor?.apply()
    }
}
