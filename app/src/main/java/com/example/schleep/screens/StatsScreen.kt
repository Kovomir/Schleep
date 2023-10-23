package com.example.schleep.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.schleep.components.countBedTime
import com.example.schleep.components.getSleepLength
import com.example.schleep.components.getSleepRecordsAfterDate
import com.example.schleep.components.toLocalDateTime
import com.example.schleep.db.SleepRecordRepository
import com.example.schleep.db.UserSettingsRepository
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime


@Composable
fun StatsScreen(
    sleepRecordRepository: SleepRecordRepository, userSettingsRepository: UserSettingsRepository
) {/*TODO MOVE TO REPOSITORY - data layer should contain bussines logic, not UI layer!*/
    val sleepRecords = sleepRecordRepository.getAllCompleteSleepRecords()
    val userSettings = userSettingsRepository.getUserSettings()
    val targetSleepTime = LocalTime.parse(userSettings.targetSleepTime)
    val targetWakeUpTime = LocalTime.parse(userSettings.wakeUpTime)
    val targetSleepStartTime = countBedTime(targetWakeUpTime, targetSleepTime)
    //convert LocalDateTime into duration from midnight
    val targetSleepDuration =
        Duration.between(targetSleepTime?.with(LocalTime.MIDNIGHT), targetSleepTime)
    val sleepRecordCount = sleepRecords.size.toString()

    var sleepDurationSum = 0
    val averageSleepDuration: String

    for (sleepRecord in sleepRecords) {
        sleepDurationSum += getSleepLength(sleepRecord).toMinutes().toInt()
        //Log.d("DEBUGTAG", "Value: ${sleepDurationSum} + pricteno ${getSleepLength(sleepRecord)}")
    }

    if (sleepRecords.isEmpty()) {
        averageSleepDuration = "Nemáte žádné záznamy spánku."
    } else {
        val averageSleepDurationInMinutes = sleepDurationSum / sleepRecords.size
        val hours = averageSleepDurationInMinutes / 60
        val minutes = averageSleepDurationInMinutes % 60
        averageSleepDuration = "$hours h $minutes m"
    }

    //Get sleep records from the last 7 days
    val dateBeforeOneWeek = LocalDateTime.now().with(LocalTime.MIDNIGHT).minusWeeks(1)
    val lastWeekSleepRecords = getSleepRecordsAfterDate(sleepRecords, dateBeforeOneWeek)

    var sleepDebtPastWeek = 0
    var sleepDebtPastWeekString = "Nemáte žádné záznamy z minulého týdne."
    var extraSleepPastWeek = 0
    var extraSleepPastWeekString = "Nemáte žádné záznamy z minulého týdne."

    var sleepRecordsFollowingSleepRoutine = 0

    var followingSleepRoutineString = "Nemáte žádné záznamy z minulého týdne."

    if (lastWeekSleepRecords.isNotEmpty()) {
        for (sleepRecord in lastWeekSleepRecords) {
            val sleepDuration = getSleepLength(sleepRecord)
            if (sleepDuration < targetSleepDuration) {
                sleepDebtPastWeek += (targetSleepDuration.toMinutes()
                    .toInt() - sleepDuration.toMinutes().toInt())
            } else {
                extraSleepPastWeek += (sleepDuration.toMinutes()
                    .toInt() - targetSleepDuration.toMinutes().toInt())
            }

            val startTime = toLocalDateTime(sleepRecord.startTime)!!.toLocalTime()
            val endTime = toLocalDateTime(sleepRecord.endTime)!!.toLocalTime()

            //add 15 minute tolerance from both sides
            val startTimeWithinInterval =
                (startTime >= targetSleepStartTime.minusMinutes(15)
                        && startTime <= targetSleepStartTime.plusMinutes(15))
            val endTimeWithinInterval =
                (endTime >= targetWakeUpTime.minusMinutes(15)
                        && endTime <= targetWakeUpTime.plusMinutes(15))

            //add 30 minute offset because of corner cases with minimal startTime and maximal endTime
            val aboveMin = sleepDuration.compareTo(targetSleepDuration.minusMinutes(30)) != -1
            val underMax = sleepDuration.compareTo(targetSleepDuration.plusMinutes(30)) < 1
            val sleepDurationWithinInterval = aboveMin && underMax

            if (sleepDurationWithinInterval && startTimeWithinInterval && endTimeWithinInterval) {
                sleepRecordsFollowingSleepRoutine++
            }
        }

        val sleepDebtHours = sleepDebtPastWeek / 60
        val sleepDebtMinutes = sleepDebtPastWeek % 60
        sleepDebtPastWeekString = "$sleepDebtHours h $sleepDebtMinutes m"

        val extraSleepHours = extraSleepPastWeek / 60
        val extraSleepMinutes = extraSleepPastWeek % 60
        extraSleepPastWeekString = "$extraSleepHours h $extraSleepMinutes m"

        // count percentage of records that the user managed to stick to the sleeping routine in the last 7 days
        val followingSleepRoutinePercentage =
            ((sleepRecordsFollowingSleepRoutine.toDouble() / lastWeekSleepRecords.size) * 100)
        followingSleepRoutineString = String.format("%.1f", followingSleepRoutinePercentage) + " %"
    }

    //TODO END MOVE TO REPOSITORY
    val scrollState = rememberScrollState()

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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
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
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = sleepRecordCount,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.fillMaxWidth(),
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
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = averageSleepDuration,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.fillMaxWidth(),
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
                        text = "Úspěšnost dodržení spánkové rutiny za poslední týden:",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = followingSleepRoutineString,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(horizontal = 10.dp),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "($sleepRecordsFollowingSleepRoutine z ${lastWeekSleepRecords.size})",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.titleSmall,

                            textAlign = TextAlign.Center
                        )
                    }

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
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = sleepDebtPastWeekString,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.fillMaxWidth(),
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
                        text = "Spánek navíc za poslední týden:",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = extraSleepPastWeekString,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }

            }
        }
    }
}
