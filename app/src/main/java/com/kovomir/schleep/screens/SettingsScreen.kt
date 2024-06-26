package com.kovomir.schleep.screens

import android.content.ContentValues
import android.content.Context
import android.util.Log
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.kovomir.schleep.db.UserSettingsRepository
import com.kovomir.schleep.utils.countBedTime
import com.kovomir.schleep.utils.isInternetAvailable
import com.kovomir.schleep.utils.notifications.RemindersManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SettingsScreen(userSettingsRepository: UserSettingsRepository, appContext: Context) {
    val firestoreDatabase = Firebase.firestore
    val currentUser = Firebase.auth.currentUser
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

    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val updateScoreScope = CoroutineScope(Dispatchers.IO)


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
                    .verticalScroll(scrollState)
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
                                initialTime = targetSleepTime,
                                pickedTime = targetSleepTime,
                                onTimePicked = {
                                    targetSleepTime = it
                                    userSettings.targetSleepTime = targetSleepTime.toString()
                                    userSettingsRepository.updateUserSettings(userSettings)
                                    bedTime = countBedTime(wakeUpTime, targetSleepTime)
                                    RemindersManager.restartReminder(appContext)
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
                                initialTime = wakeUpTime,
                                pickedTime = wakeUpTime,
                                onTimePicked = {
                                    wakeUpTime = it
                                    userSettings.wakeUpTime = wakeUpTime.toString()
                                    userSettingsRepository.updateUserSettings(userSettings)
                                    bedTime = countBedTime(wakeUpTime, targetSleepTime)
                                    RemindersManager.restartReminder(appContext)
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
                Spacer(modifier = Modifier.height(10.dp))

                Card(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = MaterialTheme.shapes.large
                        )
                        .padding(all = 10.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Změna uživatelského jména",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Jak chcete být zobrazován v žebříčku?",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(5.dp))


                        val maxNameLength = 20
                        val keyboardController = LocalSoftwareKeyboardController.current
                        var newUserName by remember { mutableStateOf(userSettings.userName) }
                        OutlinedTextField(
                            modifier = Modifier
                                .width(280.dp)
                                .padding(horizontal = 15.dp),
                            value = newUserName,
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(
                                onDone = { keyboardController?.hide() }),
                            onValueChange = { if (it.length <= maxNameLength) newUserName = it },
                            placeholder = { Text("Tvé jméno") },
                            label = { Text(text = "Jak se jmenuješ?") },
                            supportingText = {
                                Text(
                                    textAlign = TextAlign.End,
                                    modifier = Modifier.fillMaxWidth(),
                                    text = "${newUserName.length} / ${maxNameLength}",
                                )
                            },
                        )
                        Button(enabled = newUserName.isNotBlank(),
                            modifier = Modifier.padding(10.dp),
                            onClick = {
                                if (currentUser == null) {
                                    userSettings.userName = newUserName
                                    userSettingsRepository.updateUserSettings(userSettings)
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar(
                                            "Jméno změněno",
                                            withDismissAction = true
                                        )
                                    }
                                } else if (isInternetAvailable(appContext)) {
                                    //update local db
                                    userSettings.userName = newUserName
                                    userSettingsRepository.updateUserSettings(userSettings)
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar(
                                            "Jméno změněno",
                                            withDismissAction = true
                                        )
                                    }

                                    updateScoreScope.launch {
                                        //update firestore
                                        firestoreDatabase.collection("userScores")
                                            .document(currentUser.uid)
                                            .update("userName", newUserName)
                                            .addOnSuccessListener {
                                                Log.d(
                                                    ContentValues.TAG,
                                                    "DocumentSnapshot successfully written!"
                                                )
                                            }
                                            .addOnFailureListener { e ->
                                                Log.w(
                                                    ContentValues.TAG,
                                                    "Error writing document",
                                                    e
                                                )
                                            }
                                    }
                                } else {
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar(
                                            "Nejdříve se připojte k internetu.",
                                            withDismissAction = true
                                        )
                                    }
                                }
                            }) {
                            Text(text = "Potvrdit změnu")
                        }
                    }
                }
            }
        }
    }
}