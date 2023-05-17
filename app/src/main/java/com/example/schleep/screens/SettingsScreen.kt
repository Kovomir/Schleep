package com.example.schleep.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.schleep.db.UserSettingsRepository
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun SettingsScreen(userSettingsRepository: UserSettingsRepository) {

    val userSettings = userSettingsRepository.getUserSettings()

    var wakeUpTime by remember {
        mutableStateOf(LocalTime.parse(userSettings.wakeUpTime))
    }
    val formattedWakeUpTime by remember {
        derivedStateOf {
            DateTimeFormatter
                .ofPattern("HH:mm")
                .format(wakeUpTime)
        }
    }

    var targetSleepTime by remember {
        mutableStateOf(LocalTime.parse(userSettings.targetSleepTime))
    }

    var bedTime by remember {
        mutableStateOf(countBedTime(wakeUpTime, targetSleepTime))
    }

    val formattedBedTime by remember {
        derivedStateOf {
            DateTimeFormatter
                .ofPattern("HH:mm")
                .format(bedTime)
        }
    }

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
                text = "Nastavení",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            Card(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = MaterialTheme.shapes.large
                    )
                    .padding(all = 10.dp)
            ) {
                Text(
                    text = "Nastavení cílené doby spánku",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Box(
                    modifier = Modifier
                        .padding(all = 10.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Cílená doba spánku",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        TimePickerScreen(
                            pickedTime = targetSleepTime,
                            onTimePicked = {
                                targetSleepTime = it
                                userSettings.targetSleepTime = targetSleepTime.toString()
                                userSettingsRepository.updateUserSettings(userSettings)
                                bedTime = countBedTime(wakeUpTime, targetSleepTime)
                            }
                        )
                    }
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
                    text = "Nastavení spánkové rutiny",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Column(
                    modifier = Modifier
                        .padding(all = 10.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "V kolik hodin chcete vstávat?",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        TimePickerScreen(
                            pickedTime = wakeUpTime,
                            onTimePicked = {
                                wakeUpTime = it
                                userSettings.wakeUpTime = wakeUpTime.toString()
                                userSettingsRepository.updateUserSettings(userSettings)
                                bedTime = countBedTime(wakeUpTime, targetSleepTime)
                            }
                        )
                    }
                    Spacer(modifier = Modifier.width(30.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                color = MaterialTheme.colorScheme.tertiary,
                                shape = MaterialTheme.shapes.medium
                            )
                            .padding(all = 10.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = "Spát půjdete v: \n${formattedBedTime}",
                            color = MaterialTheme.colorScheme.onTertiary,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.width(40.dp))
                        Text(
                            text = "Vstanete v: \n${formattedWakeUpTime}",
                            color = MaterialTheme.colorScheme.onTertiary,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

fun countBedTime(wakeUpTime: LocalTime, targetSleepTime: LocalTime): LocalTime {
    return wakeUpTime.minusHours(targetSleepTime.hour.toLong())
        .minusMinutes(targetSleepTime.minute.toLong())
}
