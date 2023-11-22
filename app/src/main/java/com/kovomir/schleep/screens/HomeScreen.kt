package com.kovomir.schleep.screens

import android.content.ContentValues.TAG
import android.util.Log
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
import com.kovomir.schleep.db.SleepRecord
import com.kovomir.schleep.db.SleepRecordRepository
import com.kovomir.schleep.db.UserSettings
import com.kovomir.schleep.utils.UserHighScore
import com.kovomir.schleep.utils.countBedTime
import com.kovomir.schleep.utils.followedSleepRoutine
import com.kovomir.schleep.utils.fromLocalDateTime
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime


@Composable
fun HomeScreen(
    sleepRecordRepository: SleepRecordRepository,
    firestoreDatabase: FirebaseFirestore,
    firebaseAuth: FirebaseAuth,
    userSettings: UserSettings
) {
    var lastSleepRecord by remember { mutableStateOf(sleepRecordRepository.getLastSleepRecord()) }
    var lastSleepRecordEndTime by remember { mutableStateOf(lastSleepRecord.endTime) }
    var lastSleepRecordStartTime by remember { mutableStateOf(lastSleepRecord.startTime) }

    var usableStartButton by remember {
        mutableStateOf(true)
    }

    if (lastSleepRecordStartTime == "" && lastSleepRecordEndTime == "") {
        usableStartButton = true
    } else usableStartButton = !(lastSleepRecordStartTime != "" && lastSleepRecordEndTime == "")

    val targetSleepTime = LocalTime.parse(userSettings.targetSleepTime)
    val targetWakeUpTime = LocalTime.parse(userSettings.wakeUpTime)
    val targetSleepStartTime = countBedTime(targetWakeUpTime, targetSleepTime)
    //convert LocalDateTime into duration from midnight
    val targetSleepDuration =
        Duration.between(targetSleepTime?.with(LocalTime.MIDNIGHT), targetSleepTime)

    val snackbarHostState = remember { SnackbarHostState() }
    // Create a coroutine scope
    val coroutineScope = rememberCoroutineScope()

    val currentUser = firebaseAuth.currentUser
    var userScore: Int
    var userHighScore: Int

    suspend fun getCurrentUserData(): UserHighScore? {
        var currentUserScore: UserHighScore? = null
        if (currentUser != null) {
            try {
                val userScoresCollection = firestoreDatabase.collection("userScores")
                val documentSnapshot = userScoresCollection.document(currentUser.uid).get().await()

                if (documentSnapshot.exists()) {
                    currentUserScore = documentSnapshot.toObject<UserHighScore>()
                } else {
                    currentUserScore = UserHighScore(currentUser.uid, userSettings.userName, 0, 0)
                }
            } catch (e: Exception) {
                Log.e("Error", "Error fetching user data: ${e.message}")
            }
        }
        return currentUserScore
    }

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
                    text = "Spánek",
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
                                        text = "Než půjdeš večer spát, spusť záznam spánku a při vstávání ráno jej ukonči.",
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
                                                    //update Room database
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

                                                    var currentUserHighScore :UserHighScore?
                                                    //update Firestore leaderboard
                                                    if (currentUser != null) {
                                                        CoroutineScope(Dispatchers.IO).launch {
                                                            runBlocking{currentUserHighScore =
                                                               getCurrentUserData()}
                                                            userScore =
                                                                currentUserHighScore!!.score
                                                            userHighScore =
                                                                currentUserHighScore!!.highestScore

                                                            if (followedSleepRoutine(
                                                                    lastSleepRecord,
                                                                    targetSleepDuration,
                                                                    targetSleepStartTime,
                                                                    targetWakeUpTime
                                                                ) && targetSleepDuration >= Duration.ofHours(6)
                                                            ) {
                                                                userScore++
                                                            }

                                                            if (userScore > userHighScore) {
                                                                userHighScore = userScore
                                                            }
                                                            val newUserScore = UserHighScore(
                                                                currentUser.uid,
                                                                userSettings.userName,
                                                                userScore,
                                                                userHighScore
                                                            )

                                                            firestoreDatabase.collection("userScores")
                                                                .document(newUserScore.userId)
                                                                .set(newUserScore)
                                                                .addOnSuccessListener {
                                                                    Log.d(
                                                                        TAG,
                                                                        "DocumentSnapshot successfully written!"
                                                                    )
                                                                }
                                                                .addOnFailureListener { e ->
                                                                    Log.w(
                                                                        TAG,
                                                                        "Error writing document",
                                                                        e
                                                                    )
                                                                }
                                                        }
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
                                    Spacer(modifier = Modifier.height(15.dp))
                                    if(currentUser != null){
                                        Text(
                                            text = "Pro navýšení skóre se musíš připojit k internetu a tvá cílená doba spánku musí být alespoň 6 hodin.",
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            style = MaterialTheme.typography.titleMedium,
                                            textAlign = TextAlign.Center
                                        )
                                    } else {
                                        Text(
                                            text = "Pro záznam do žebříčku nejlepších se do něj musíš nejprve přihlásit na záložce statistiky.",
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            style = MaterialTheme.typography.titleMedium,
                                            textAlign = TextAlign.Center
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