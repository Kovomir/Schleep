package com.example.schleep

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.compose.SchleepTheme
import com.example.schleep.db.SchleepDatabase
import com.example.schleep.db.SleepRecordRepository
import com.example.schleep.db.UserSettingsRepository

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
                        sleepRecordRepository = sleepRecordRepository
                    )
                }
            }
        }
    }
}
