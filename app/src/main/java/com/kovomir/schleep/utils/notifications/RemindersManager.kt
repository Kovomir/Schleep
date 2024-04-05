package com.kovomir.schleep.utils.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.kovomir.schleep.db.SchleepDatabase
import com.kovomir.schleep.db.UserSettingsRepository
import com.kovomir.schleep.utils.countBedTime
import java.time.LocalTime
import java.util.Calendar
import java.util.Locale

object RemindersManager {
    private const val REQUEST_CODE = 200
    const val CHANNEL_ID = "ALARM_CHANNEL"

    fun startReminder(
        context: Context,
        reminderId: Int = REQUEST_CODE
    ) {
        val schleepDatabase = SchleepDatabase.getDatabase(context)
        val userSettingsRepository = UserSettingsRepository(schleepDatabase.userSettingsDao)
        val userSettings = userSettingsRepository.getUserSettings()

        val targetSleepTime = LocalTime.parse(userSettings.targetSleepTime)
        val targetWakeUpTime = LocalTime.parse(userSettings.wakeUpTime)
        val notificationLaunchTime =
            countBedTime(targetWakeUpTime, targetSleepTime).minusMinutes(15)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent =
            Intent(context.applicationContext, NotificationReceiver::class.java).let { intent ->
                PendingIntent.getBroadcast(
                    context.applicationContext,
                    reminderId,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            }

        val calendar: Calendar = Calendar.getInstance(Locale.ENGLISH).apply {
            set(Calendar.HOUR_OF_DAY, notificationLaunchTime.hour)
            set(Calendar.MINUTE, notificationLaunchTime.minute)
        }

        //one minute added, so that the notification does not fire again when the user clicks immediately
        if (Calendar.getInstance(Locale.ENGLISH)
                .apply { add(Calendar.MINUTE, 1) }.timeInMillis - calendar.timeInMillis > 0
        ) {
            //add one day, if the time is in the past
            calendar.add(Calendar.DATE, 1)
        }

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            intent
        )

    }

    fun stopReminder(
        context: Context,
        reminderId: Int = REQUEST_CODE
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(
                context,
                reminderId,
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )
        }
        alarmManager.cancel(intent)
    }

    fun restartReminder(context: Context) {
        stopReminder(context)
        startReminder(context)
    }
}