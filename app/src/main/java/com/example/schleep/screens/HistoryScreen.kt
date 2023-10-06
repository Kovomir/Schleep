package com.example.schleep.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.schleep.components.getDayOfWeekAsString
import com.example.schleep.components.getSleepRecordLenght
import com.example.schleep.components.toLocalDateTime
import com.example.schleep.db.SleepRecordRepository
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HistoryScreen(sleepRecordRepository: SleepRecordRepository) {
    val sleepRecords = sleepRecordRepository.getAllCompleteSleepRecords()
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(all = 10.dp)
                .background(color = MaterialTheme.colorScheme.background)
                .fillMaxWidth()
        ) {
            stickyHeader {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    Text(
                        text = "Historie",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.headlineLarge,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(50.dp))
                }
            }
            for (sleepRecord in sleepRecords) {
                val startTime = toLocalDateTime(sleepRecord.startTime)
                val endTime = toLocalDateTime(sleepRecord.endTime)

                val formatter = DateTimeFormatter.ofPattern("d.M.yyyy H:mm")
                val formatedStartTime = startTime?.format(formatter)
                val formatedEndTime = endTime?.format(formatter)

                val startTimeDayOfWeek = getDayOfWeekAsString(startTime)
                val endTimeDayOfWeek = getDayOfWeekAsString(endTime)

                item {
                    var hidden by remember {
                        mutableStateOf(false)
                    }
                    AnimatedVisibility(
                        visible = !hidden,
                        enter = expandVertically(),
                        exit = shrinkVertically(animationSpec = tween(durationMillis = 250))
                    ) {
                        Card(
                            modifier = Modifier
                                .background(
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    shape = MaterialTheme.shapes.large
                                )
                                .padding(all = 10.dp)
                                .fillMaxSize()
                        ) {
                            Box(modifier = Modifier.padding(vertical = 0.dp)) {
                                Text(
                                    text = "Začátek:",
                                    color = MaterialTheme.colorScheme.primary,
                                    style = MaterialTheme.typography.titleLarge,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .align(Alignment.BottomCenter),
                                    textAlign = TextAlign.Center
                                )

                                IconButton(modifier = Modifier.align(Alignment.CenterEnd),
                                    onClick = {
                                        hidden = true
                                        sleepRecordRepository.deleteSleepRecordById(sleepRecord.id)

                                    }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Smazat záznam")
                                }
                            }

                            Text(
                                text = startTimeDayOfWeek + " " + formatedStartTime.toString(),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier
                                    .fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(
                                text = "Konec:",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier
                                    .fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = endTimeDayOfWeek + " " + formatedEndTime.toString(),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier
                                    .fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(
                                text = "Délka spánku:",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier
                                    .fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = getSleepRecordLenght(sleepRecord),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier
                                    .fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}