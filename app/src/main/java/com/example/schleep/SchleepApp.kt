package com.example.schleep

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.example.schleep.components.alarm.AlarmNotificationService

class SchleepApp: Application() {
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            AlarmNotificationService.ALARM_CHANNEL_ID,
            "alarm_notification",
            NotificationManager.IMPORTANCE_HIGH
        )

        channel.description = "Používané pro notifikace budíku"
        val notificationManager = getSystemService((Context.NOTIFICATION_SERVICE)) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}