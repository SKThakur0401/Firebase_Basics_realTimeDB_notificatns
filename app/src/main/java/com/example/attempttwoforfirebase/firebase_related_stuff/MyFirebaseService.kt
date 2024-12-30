package com.example.attempttwoforfirebase.firebase_related_stuff

import android.Manifest
import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.attempttwoforfirebase.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        message.notification?.let {
            val contentTitle = it.title
            val contentText = it.body
            // Handle the notification message here

            requestNotificationPermission(contentTitle, contentText)
        }
    }

    private fun requestNotificationPermission(contentTitle: String?, contentText: String?) {

        // In newer android versions it is mandatory to take user permission before sending him notifications!
        // That is y on phone u get pop-up to allow notifications then only notificatinos are allowed on phone
        // So this function checks if phone user has a high Android version or not,
        // If user has a high Android version (Android 13+  , it checks whether user has granted permission or not)
        // if granted, user will see notifications else not , for lower versions user will anyway see notifications
        // The request to grant permissions for sending notifications is requested to user from "MainActivity"
        // "requestNotificationPermission" function
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED
            ) {
                sendNotification(contentTitle, contentText)
            } else{
                Log.d("TAG", "requestNotificationPermission: Permission not granted")
            }
        } else {
            sendNotification(contentTitle, contentText)      // For older android versions it is simply sendNotifications()    :)
        }
    }

    private fun sendNotification(contentTitle: String?, contentText: String?) {
        // Implement the logic to send the notification

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val myNotification = Notification.Builder(this)
            .setContentTitle(contentTitle)
            .setContentText(contentText)
            .setSmallIcon(R.drawable.svg_notification)
            .build()

        notificationManager.notify(1, myNotification)
    }

}