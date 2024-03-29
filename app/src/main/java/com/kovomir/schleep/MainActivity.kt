package com.kovomir.schleep

import android.os.Bundle
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.kovomir.compose.SchleepTheme
import com.kovomir.schleep.db.SchleepDatabase
import com.kovomir.schleep.db.SleepRecordRepository
import com.kovomir.schleep.db.UserSettingsRepository


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //hides app title, which was visible on Android api level 33
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        val db by lazy { SchleepDatabase.getDatabase(applicationContext) }

        val userSettingsRepository = UserSettingsRepository(db.userSettingsDao)
        val sleepRecordRepository = SleepRecordRepository(db.sleepRecordDao)

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
}
