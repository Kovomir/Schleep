package com.kovomir.schleep

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Bundle
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.kovomir.compose.SchleepTheme
import com.kovomir.schleep.db.SchleepDatabase
import com.kovomir.schleep.db.SleepRecordRepository
import com.kovomir.schleep.db.UserSettingsRepository
import com.kovomir.schleep.utils.notifications.RemindersManager


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //hides app title, which was visible on Android api level 33
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        val db by lazy { SchleepDatabase.getDatabase(applicationContext) }

        val userSettingsRepository = UserSettingsRepository(db.userSettingsDao)
        val sleepRecordRepository = SleepRecordRepository(db.sleepRecordDao)
        createNotificationsChannels()
        RemindersManager.startReminder(this)

        setContent {
            SchleepTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(
                        userSettingsRepository = userSettingsRepository,
                        sleepRecordRepository = sleepRecordRepository,
                        appContext = applicationContext
                    )
                }
            }
        }
    }

    private fun createNotificationsChannels() {
        val channel = NotificationChannel(
            RemindersManager.ALARM_CHANNEL_ID,
            "TIME_TO_SLEEP",
            NotificationManager.IMPORTANCE_HIGH
        )
        ContextCompat.getSystemService(this, NotificationManager::class.java)
            ?.createNotificationChannel(channel)
    }
}
