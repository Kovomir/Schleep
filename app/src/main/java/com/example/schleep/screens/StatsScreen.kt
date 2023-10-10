package com.example.schleep.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.schleep.components.HorizontalLine
import com.example.schleep.components.getSleepLength
import com.example.schleep.db.SleepRecordRepository

@Composable
fun StatsScreen(sleepRecordRepository: SleepRecordRepository) {
    /*TODO MOVE TO REPOSITORY - data layer should contain bussines logic, not UI layer!*/
    val sleepRecords = sleepRecordRepository.getAllCompleteSleepRecords()
    var sleepDurationSum = 0
    for (sleepRecord in sleepRecords) {
        sleepDurationSum += getSleepLength(sleepRecord).toMinutes().toInt()
        //Log.d("DEBUGTAG", "Value: ${sleepDurationSum} + pricteno ${getSleepLength(sleepRecord)}")
    }
    var averageSleepDuration = ""

    if (sleepRecords.isEmpty()) {
        averageSleepDuration = "Nemáte žádné záznamy spánku."
    } else {
        val averageSleepDurationInMinutes = sleepDurationSum / sleepRecords.size
        val hours = averageSleepDurationInMinutes / 60
        val minutes = averageSleepDurationInMinutes % 60
        averageSleepDuration = "$hours h $minutes m"
    }

    val sleepRecordCount = sleepRecords.size.toString()

    //TODO MOVE TO REPOSITORY

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(all = 5.dp)
                .background(color = MaterialTheme.colorScheme.background)
                .fillMaxWidth()
        ) {
            Text(
                text = "Statistics",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(10.dp))
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Card(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = MaterialTheme.shapes.large
                        )
                        .padding(all = 10.dp)
                        .fillMaxSize()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = "Počet záznamů:",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = sleepRecordCount,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        HorizontalLine()
                        Text(
                            text = "Průměrná délka spánku:",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = averageSleepDuration,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        /*Text(
                            text = averageSleepDuration,
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            textAlign = TextAlign.Center
                        )*/
                        // TODO STATISTIKY TEXTY


                    }
                }
            }
        }
    }
}