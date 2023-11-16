package com.example.schleep.screens

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.schleep.R
import com.example.schleep.utils.LEADERBOARDS_ROUTE
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun SignInScreen(
    firebaseAuth: FirebaseAuth,
    appContext: Context,
    navController: NavController,
    oneTapClient: SignInClient
) {
    var signedInUser by remember {
        mutableStateOf(firebaseAuth.currentUser)
    }

    if (signedInUser != null) {
        navController.popBackStack()
        navController.navigate(LEADERBOARDS_ROUTE)
    }

    val signInRequest = BeginSignInRequest.builder().setGoogleIdTokenRequestOptions(
        BeginSignInRequest.GoogleIdTokenRequestOptions.builder().setSupported(true)
            .setServerClientId(appContext.getString(R.string.web_client_id))
            .setFilterByAuthorizedAccounts(false).build()
    ).setAutoSelectEnabled(true).build()

    suspend fun signIn(): IntentSender? {
        val result = try {
            oneTapClient.beginSignIn(signInRequest).await()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            return null
        }
        return result?.pendingIntent?.intentSender
    }

    suspend fun signInWithIntent(intent: Intent): FirebaseUser? {
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
        if (googleCredentials != null) {
            Log.d("DEBUG", "GOT CREDENDIALS")
        }
        return try {
            firebaseAuth.signInWithCredential(googleCredentials).await().user
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            null
        }
    }

    val scope = CoroutineScope(Dispatchers.Main)

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartIntentSenderForResult(),
            onResult = { result ->

                if (result.resultCode == RESULT_OK) {
                    scope.launch {
                        signedInUser = signInWithIntent(intent = result.data ?: return@launch)
                        Log.d("DEBUGsignin", "$signedInUser")
                    }
                } else Log.d(
                    "DEBUGsignin",
                    "RESULT NOT OK: result code: ${result.resultCode}\n data: ${result.data.toString()}"
                )
            })

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
                text = "Přihlášení uživatele",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(10.dp))
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
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                ) {
                    val painter: Painter = painterResource(id = R.drawable.achievement)
                    Image(
                        painter = painter, contentDescription = "achievement",
                        contentScale = ContentScale.Fit,
                        alignment = Alignment.Center
                    )
                    Text(
                        text = "Přihlašte se a soupeřte s ostatními uživateli v dodržování spánkové rutiny." + " Pro navýšení skóre musí být vaše cílená doba spánku natavena na alespoň 6 hodin.",
                        textAlign = TextAlign.Justify,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,

                        )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(modifier = Modifier.background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = MaterialTheme.shapes.large
                    ), onClick = {
                        scope.launch {
                            val signInIntentSender = signIn()
                            launcher.launch(
                                IntentSenderRequest.Builder(
                                    signInIntentSender ?: return@launch
                                ).build()
                            )
                        }
                    }) {
                        Text(
                            text = "Přihlásit se účtem Google",
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "Ujistěte se, že jste připojeni k internetu.",
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

        }

    }
}
