package com.example.schleep.screens.firebaseScreens

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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.schleep.db.UserSettingsRepository
import com.example.schleep.utils.HorizontalLine
import com.example.schleep.utils.SIGNIN_SCREEN_ROUTE
import com.example.schleep.utils.UserHighScore
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException

@Composable
fun LeaderboardScreen(
    firestoreDatabase: FirebaseFirestore,
    firebaseAuth: FirebaseAuth,
    oneTapClient: SignInClient,
    navController: NavController,
    userSettingsRepository: UserSettingsRepository
) {
    val userSettings = userSettingsRepository.getUserSettings()
    val userHighScores = firestoreDatabase.collection("userScores")

    val leaderboardScores: MutableList<UserHighScore> = mutableListOf()
    val currentUser = firebaseAuth.currentUser
    var currentUserRank = 0
    var currentUserHighScore = UserHighScore("", "", 0, 0)

    suspend fun prepareUserHighScores() {
        var documents: MutableList<DocumentSnapshot>? = null
        try {
            documents =
                userHighScores.orderBy("score", Query.Direction.DESCENDING).get().await().documents
        } catch (e: FirebaseFirestoreException) {
            Log.d("error", "error while getting data from firestore: $e")
        }

        if (documents != null) {
            for (document in documents) {
                Log.d("DEBUG", "${document.id} => ${document.data}")

                val leaderboardEntry = UserHighScore(
                    document.id,
                    document.data?.get("userName").toString(),
                    document.data?.get("score").toString().toInt(),
                    document.data?.get("highestScore").toString().toInt()
                )
                leaderboardScores.add(leaderboardEntry)

                //current logged in user
                if (currentUser != null && leaderboardEntry.userId == currentUser.uid) {
                    currentUserRank = leaderboardScores.size
                    currentUserHighScore = leaderboardEntry
                }
            }
        }
    }

    runBlocking { prepareUserHighScores() }

    suspend fun signOut() {
        try {
            oneTapClient.signOut().await()
            firebaseAuth.signOut()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
        }
    }

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
                text = "Žebříček nejlepších",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(10.dp))
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {

                Card(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = MaterialTheme.shapes.large
                        )
                        .padding(all = 10.dp)
                        .heightIn(max = 250.dp)
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text(
                            text = "Pořadí:",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                        )
                        Text(
                            text = "Uživatel:",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                        )
                        Text(
                            text = "Skóre:",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                        )

                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(scrollState)
                    ) {
                        //creates top 10 leaderboard
                        var userRank = 1
                        for (leaderboardScore in leaderboardScores) {
                            var nameColor = MaterialTheme.colorScheme.primary
                            val userName: String
                            if (leaderboardScore.userId == currentUser?.uid) {
                                nameColor = MaterialTheme.colorScheme.tertiary
                                userName = userSettings.userName
                            } else {
                                userName = leaderboardScore.userName
                            }
                            HorizontalLine(color = nameColor)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Text(
                                    text = "${userRank}.",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    style = MaterialTheme.typography.bodyLarge,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxWidth()
                                )
                                Text(
                                    text = userName,
                                    color = nameColor,
                                    style = MaterialTheme.typography.bodyLarge,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxWidth()
                                )
                                Text(
                                    text = leaderboardScore.score.toString(),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    style = MaterialTheme.typography.bodyLarge,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxWidth()
                                )
                            }
                            HorizontalLine(color = nameColor)

                            userRank++
                            if (userRank >= 10) {
                                break
                            }

                        }
                    }
                }

                Spacer(modifier = Modifier.height(25.dp))
                Card(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            shape = MaterialTheme.shapes.large
                        )
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Vaše současné skóre:",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(5.dp)
                            .fillMaxWidth()
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text(
                            text = "${currentUserRank}.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyLarge,
                        )
                        Text(
                            text = currentUserHighScore.userName,
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.bodyLarge,
                        )
                        Text(
                            text = currentUserHighScore.score.toString(),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                val scope = CoroutineScope(Dispatchers.Main)

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(all = 5.dp)
                        .background(color = MaterialTheme.colorScheme.background)
                        .fillMaxWidth()
                ) {
                    Button(onClick = {
                        scope.launch {
                            signOut()
                            navController.popBackStack()
                            navController.navigate(SIGNIN_SCREEN_ROUTE)
                        }
                    }) {
                        Text(text = "Odhlásit se")
                    }
                }

            }
        }
    }
}