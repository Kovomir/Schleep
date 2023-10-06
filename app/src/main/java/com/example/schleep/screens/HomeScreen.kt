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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.schleep.components.fromLocalDateTime
import com.example.schleep.db.SleepRecord
import com.example.schleep.db.SleepRecordRepository
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(sleepRecordRepository: SleepRecordRepository) {
    var lastSleepRecord by remember { mutableStateOf(sleepRecordRepository.getLastSleepRecord()) }
    var lastSleepRecordEndTime by remember { mutableStateOf(lastSleepRecord.endTime) }
    var lastSleepRecordStartTime by remember { mutableStateOf(lastSleepRecord.startTime) }

    var usableStartButton by remember {
        mutableStateOf(true)
    }

    if (lastSleepRecordStartTime == "" && lastSleepRecordEndTime == "") {
        usableStartButton = true
    } else usableStartButton = !(lastSleepRecordStartTime != "" && lastSleepRecordEndTime == "")

    val snackbarHostState = remember { SnackbarHostState() }
    // Create a coroutine scope
    val coroutineScope = rememberCoroutineScope()

    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
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
                    text = "Budík",
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
                                text = "Zaznamenat spánek",
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
                                        text = "Než půjdete spát, spusťte záznam spánku a při vstávání ráno jej ukončete.",
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        style = MaterialTheme.typography.titleMedium,
                                        textAlign = TextAlign.Center
                                    )
                                    Spacer(modifier = Modifier.height(15.dp))
                                    Box {
                                        if (usableStartButton) {
                                            Button(
                                                modifier = Modifier.background(
                                                    color = MaterialTheme.colorScheme.primary,
                                                    shape = MaterialTheme.shapes.large
                                                ),
                                                enabled = usableStartButton,
                                                onClick = {
                                                    lastSleepRecord = SleepRecord(
                                                        startTime = fromLocalDateTime(LocalDateTime.now())
                                                    )
                                                    lastSleepRecordEndTime = lastSleepRecord.endTime
                                                    lastSleepRecordStartTime =
                                                        lastSleepRecord.startTime
                                                    sleepRecordRepository.insertSleepRecord(
                                                        lastSleepRecord
                                                    )
                                                    lastSleepRecord =
                                                        sleepRecordRepository.getLastSleepRecord()
                                                    coroutineScope.launch {
                                                        snackbarHostState.showSnackbar(
                                                            "Zaznamenávání spuštěno",
                                                            withDismissAction = true
                                                        )
                                                    }
                                                },
                                            ) {
                                                Text(
                                                    text = "Jít spát",
                                                    color = MaterialTheme.colorScheme.onPrimary,
                                                    style = MaterialTheme.typography.headlineLarge
                                                )
                                            }
                                        } else {
                                            Button(
                                                modifier = Modifier.background(
                                                    color = MaterialTheme.colorScheme.primary,
                                                    shape = MaterialTheme.shapes.large
                                                ),
                                                enabled = !usableStartButton,
                                                onClick = {
                                                    lastSleepRecord.endTime =
                                                        fromLocalDateTime(LocalDateTime.now())
                                                    lastSleepRecordEndTime = lastSleepRecord.endTime
                                                    sleepRecordRepository.updateSleepRecord(
                                                        lastSleepRecord
                                                    )
                                                    lastSleepRecord =
                                                        sleepRecordRepository.getLastSleepRecord()
                                                    coroutineScope.launch {
                                                        snackbarHostState.showSnackbar(
                                                            "Zaznamenávání ukončeno",
                                                            withDismissAction = true
                                                        )
                                                    }
                                                },
                                            ) {
                                                Text(
                                                    text = "Konec spánku",
                                                    color = MaterialTheme.colorScheme.onPrimary,
                                                    style = MaterialTheme.typography.headlineLarge
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


