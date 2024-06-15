package com.sampah.banksampahdigital.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.sampah.banksampahdigital.R

class StatusMonitoringService : Service() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        firestore = FirebaseFirestore.getInstance()
        sharedPreferences = getSharedPreferences("NotificationPref", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()

        // Memantau perubahan status secara realtime
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.uid?.let { userId ->
            firestore.collection("users")
                .document(userId)
                .collection("TrashSent")
                .addSnapshotListener { snapshots, e ->
                    if (e != null) {
                        Log.w(TAG, "Listen failed", e)
                        return@addSnapshotListener
                    }

                    for (document in snapshots!!.documentChanges) {
                        if (document.type == DocumentChange.Type.MODIFIED) {
                            val status = document.document.getString("status")
                            val documentId = document.document.id

                            if (status != null && (status == "di terima" || status == "di tolak")) {
                                val isNotified = sharedPreferences.getBoolean(documentId, false)
                                if (!isNotified) {
                                    sendNotification("Penjemputan Sampah", "Permintaan Anda telah $status")
                                    editor.putBoolean(documentId, true)
                                    editor.apply()
                                    Log.e(TAG, "sendNotification: $status")
                                }
                            }
                        }
                    }
                }
        }
    }

    private fun sendNotification(title: String, message: String) {
        val notificationManager = ContextCompat.getSystemService(this, NotificationManager::class.java) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("default", "Channel name", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, "default")
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_notifications_24)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notificationManager.notify(1, notification)
    }

    companion object {
        private const val TAG = "StatusMonitoringService"
    }
}
