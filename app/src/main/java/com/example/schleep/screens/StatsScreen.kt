package com.example.schleep.screens

import androidx.compose.foundation.background
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
import com.example.schleep.components.getSleepLength
import com.example.schleep.components.getSleepRecordsAfterDate
import com.example.schleep.components.toLocalTime
import com.example.schleep.db.SleepRecordRepository
import com.example.schleep.db.UserSettingsRepository
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime


@Composable
fun StatsScreen(
    sleepRecordRepository: SleepRecordRepository,
    userSettingsRepository: UserSettingsRepository
) {
    /*TODO MOVE TO REPOSITORY - data layer should contain bussines logic, not UI layer!*/
    val sleepRecords = sleepRecordRepository.getAllCompleteSleepRecords()
    val userSettings = userSettingsRepository.getUserSettings()

    var sleepDurationSum = 0

    for (sleepRecord in sleepRecords) {
        sleepDurationSum += getSleepLength(sleepRecord).toMinutes().toInt()
        //Log.d("DEBUGTAG", "Value: ${sleepDurationSum} + pricteno ${getSleepLength(sleepRecord)}")
    }

    val averageSleepDuration: String

    if (sleepRecords.isEmpty()) {
        averageSleepDuration = "Nemáte žádné záznamy spánku."
    } else {
        val averageSleepDurationInMinutes = sleepDurationSum / sleepRecords.size
        val hours = averageSleepDurationInMinutes / 60
        val minutes = averageSleepDurationInMinutes % 60
        averageSleepDuration = "$hours h $minutes m"
    }

    val sleepRecordCount = sleepRecords.size.toString()

    //Get sleep records from the last 7 days
    val dateBeforeOneWeek = LocalDateTime.now().with(LocalTime.MIDNIGHT).minusWeeks(1)
    val lastWeekSleepRecords = getSleepRecordsAfterDate(sleepRecords, dateBeforeOneWeek)

    val targetSleepTime = toLocalTime(userSettings.targetSleepTime)
    //convert LocalDateTime into duration from midnight
    val targetSleepDuration = Duration.between(targetSleepTime?.with(LocalTime.MIDNIGHT), targetSleepTime)

    var sleepDebtPastWeek = 0
    var sleepDebtPastWeekString = "Nemáte žádné záznamy z minulého týdne."
    if(lastWeekSleepRecords.isNotEmpty()){
        for (sleepRecord in lastWeekSleepRecords) {
            val sleepDuration = getSleepLength(sleepRecord)
            if(sleepDuration < targetSleepDuration) {
                sleepDebtPastWeek += (targetSleepDuration.toMinutes().toInt() - sleepDuration.toMinutes().toInt())
            }
        }
        val hours = sleepDebtPastWeek / 60
        val minutes = sleepDebtPastWeek % 60

        sleepDebtPastWeekString = "$hours h $minutes m"
    }

    //TODO END MOVE TO REPOSITORY

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
                text = "Statistiky",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(10.dp))
            Column(modifier = Modifier.fillMaxSize()) {
                Card(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = MaterialTheme.shapes.large
                        )
                        .padding(all = 10.dp)
                ) {
                    Text(
                        text = "Celkový počet záznamů:",
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
                }

                Spacer(modifier = Modifier.height(10.dp))
                Card(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = MaterialTheme.shapes.large
                        )
                        .padding(all = 10.dp)
                ) {
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
                }

                Spacer(modifier = Modifier.height(10.dp))
                Card(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = MaterialTheme.shapes.large
                        )
                        .padding(all = 10.dp)
                ) {
                    Text(
                        text = "Spánkový dluh za poslední týden:",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = sleepDebtPastWeekString,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
                Card(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = MaterialTheme.shapes.large
                        )
                        .padding(all = 10.dp)
                ) {
                    Text(
                        text = "Lorem ipsum:",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = targetSleepDuration.toString(),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }

            }
        }
    }
}
