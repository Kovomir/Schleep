package com.kovomir.schleep.utils.notifications

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.kovomir.schleep.MainActivity
import com.kovomir.schleep.R
import com.kovomir.schleep.utils.notifications.RemindersManager.CHANNEL_ID

class NotificationReceiver : BroadcastReceiver() {
    private val NOTIFICATION_ID = 100

    override fun onReceive(context: Context, intent: Intent) {
        val notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager

        notificationManager.sendReminderNotification(
            applicationContext = context,
            channelId = CHANNEL_ID
        )

        //reschedule notification
        RemindersManager.startReminder(context.applicationContext)
    }

    private fun NotificationManager.sendReminderNotification(
        applicationContext: Context,
        channelId: String,
    ) {
        val contentIntent = Intent(applicationContext, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            1,
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val builder = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle("Čas jít spát")
            .setContentText(
                "Pro dosažení vašeho cíle byste měli za 15 minut " +
                        "ulehnout ke spánku. Nezapomeňte spustit jeho záznam."
            )
            .setSmallIcon(R.drawable.logo_rounded)
            .setStyle(
                NotificationCompat.BigTextStyle().bigText(
                    "Čas si jít čistit zuby. Pro dosažení vašeho cíle byste měli " +
                            "za 15 minut ulehnout ke spánku. Nezapomeňte spustit jeho záznam."
                )
            )
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        notify(NOTIFICATION_ID, builder.build())
    }

}
