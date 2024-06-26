package com.kovomir.schleep.screens.onboardingScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.DefaultShadowColor
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kovomir.schleep.R
import com.kovomir.schleep.utils.FIRST_SETUP_SCREEN_ROUTE
import com.kovomir.schleep.utils.WELCOME_SCREEN_ROUTE
import com.kovomir.schleep.db.UserSettingsRepository
import kotlin.text.Typography.nbsp



@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun WelcomeScreen(userSettingsRepository: UserSettingsRepository, navController: NavController) {
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
                            text = "Vítej v aplikaci Schleep",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.headlineLarge,
                            textAlign = TextAlign.Center
                        )

                        val painter: Painter = painterResource(id = R.drawable.logo_rounded)
                        Image(
                            painter = painter, contentDescription = "Schleep logo",
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.Center,
                            modifier = Modifier
                                .padding(PaddingValues(all = 15.dp))
                                .shadow(
                                    elevation = 10.dp, CircleShape,
                                    clip = true,
                                    ambientColor = DefaultShadowColor,
                                    spotColor = DefaultShadowColor
                                )
                        )

                        Text(
                            text = "Schleep ti pomůže sledovat a" + nbsp + "zlepšit tvé spánkové návyky.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(3.dp)
                        )
                        Text(
                            text = "Každý den, než půjdeš spát, spusť měření spánku a ráno, až vstaneš," +
                                    " ho ukonči. \n\nAplikace tě nyní provede krátkým procesem prvotního nastavení.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Justify,
                            modifier = Modifier.padding(20.dp)
                        )

                        val maxNameLength = 20
                        val userSettings = userSettingsRepository.getUserSettings()
                        var userName by remember { mutableStateOf(userSettings.userName) }
                        val keyboardController = LocalSoftwareKeyboardController.current
                        OutlinedTextField(
                            modifier = Modifier
                                .width(280.dp)
                                .padding(horizontal = 15.dp),
                            value = userName,
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(
                                onDone = {keyboardController?.hide()}),
                            onValueChange = { if (it.length <= maxNameLength) userName = it },
                            placeholder = { Text("Tvé jméno") },
                            label = { Text(text = "Jak se jmenuješ?")},
                            supportingText = {
                                Text(
                                    text = "${userName.length} / ${maxNameLength}",
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.End,
                                )
                            },
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            modifier = Modifier
                                .padding(10.dp),
                            enabled = userName.isNotBlank(),
                            onClick = {
                                userSettings.userName = userName
                                userSettingsRepository.updateUserSettings(
                                    userSettings
                                )

                                navController.navigate(route = FIRST_SETUP_SCREEN_ROUTE) {
                                    popUpTo(route = WELCOME_SCREEN_ROUTE) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        ) {
                            Text(
                                text = "Pokračovat",
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