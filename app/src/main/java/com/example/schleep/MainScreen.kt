package com.example.schleep


import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.schleep.db.SleepRecordRepository
import com.example.schleep.db.UserSettingsRepository
import com.example.schleep.screens.HistoryScreen
import com.example.schleep.screens.HomeScreen
import com.example.schleep.screens.SettingsScreen
import com.example.schleep.screens.SignInScreen
import com.example.schleep.screens.StatsScreen
import com.example.schleep.screens.TipsScreen
import com.example.schleep.screens.firebaseScreens.LeaderboardScreen
import com.example.schleep.screens.onboardingScreens.FirstSetupScreen
import com.example.schleep.screens.onboardingScreens.WelcomeScreen
import com.example.schleep.utils.BottomNavItem
import com.example.schleep.utils.FIRST_SETUP_SCREEN_ROUTE
import com.example.schleep.utils.LEADERBOARDS_ROUTE
import com.example.schleep.utils.SIGNIN_SCREEN_ROUTE
import com.example.schleep.utils.WELCOME_SCREEN_ROUTE
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun MainScreen(
    userSettingsRepository: UserSettingsRepository,
    sleepRecordRepository: SleepRecordRepository,
    firestoreDatabase: FirebaseFirestore,
    firebaseAuth: FirebaseAuth,
    appContext: Context
) {
    val navController = rememberNavController()
    val backStackEntry = navController.currentBackStackEntryAsState()
    val bottomNavScreens = listOf(
        BottomNavItem.Home,
        BottomNavItem.History,
        BottomNavItem.Stats,
        BottomNavItem.Settings,
        BottomNavItem.Tips
    )

    val firstLaunch = userSettingsRepository.getUserSettings().firstLaunch
    var navStartDestination = BottomNavItem.Home.route

    if (firstLaunch) {
        navStartDestination = WELCOME_SCREEN_ROUTE
    }

    val bottomNavScreensList = bottomNavScreens.map { it.route } as MutableList
    bottomNavScreensList.addAll(listOf(LEADERBOARDS_ROUTE, SIGNIN_SCREEN_ROUTE))

    val showBottomAndTopBar =
        backStackEntry.value?.destination?.route in bottomNavScreensList

    val oneTapClient = Identity.getSignInClient(appContext)
    val userSettings = userSettingsRepository.getUserSettings()

    Scaffold(
        topBar = {
            if (showBottomAndTopBar) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .background(color = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        val painter: Painter = painterResource(id = R.drawable.logo_rounded)
                        Image(
                            painter = painter,
                            contentDescription = "Image from resources",
                            contentScale = ContentScale.Fit,
                            alignment = Alignment.Center,
                            modifier = Modifier
                                .padding(3.dp)
                                .size(50.dp)
                                .clip(CircleShape)
                        )
                        Text(
                            text = "Schleep",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.displaySmall,
                            modifier = Modifier.padding(all = 3.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        },
        bottomBar = {
            if (showBottomAndTopBar) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                ) {
                    bottomNavScreens.forEach { item ->
                        val selected = item.route == backStackEntry.value?.destination?.route
                        NavigationBarItem(
                            selected = selected,
                            label = {
                                Text(
                                    text = item.name,
                                    fontWeight = FontWeight.SemiBold,
                                    softWrap = false
                                )
                            },
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = "${item.name} Icon",
                                )
                            },
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(BottomNavItem.Home.route) {
                                        saveState = true
                                    }
                                    launchSingleTop = true

                                    restoreState = item.route != BottomNavItem.Stats.route
                                }
                            }
                        )
                    }
                }
            }
        }
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .padding(15.dp)
        ) {
            NavHost(
                navController = navController,
                startDestination = navStartDestination
            ) {
                //BottomNavBar screens
                composable(route = BottomNavItem.Home.route) {
                    HomeScreen(
                        sleepRecordRepository = sleepRecordRepository,
                        firestoreDatabase = firestoreDatabase,
                        firebaseAuth = firebaseAuth,
                        userSettings = userSettings
                    )
                }
                composable(route = BottomNavItem.History.route) {
                    HistoryScreen(sleepRecordRepository = sleepRecordRepository)
                }
                composable(route = BottomNavItem.Settings.route) {
                    SettingsScreen(userSettingsRepository = userSettingsRepository)
                }
                composable(route = BottomNavItem.Stats.route) {
                    StatsScreen(
                        sleepRecordRepository = sleepRecordRepository,
                        userSettingsRepository = userSettingsRepository,
                        navController = navController,
                        firebaseAuth = firebaseAuth
                    )
                }
                composable(route = BottomNavItem.Tips.route) {
                    TipsScreen()
                }

                //Onboarding screens
                composable(route = FIRST_SETUP_SCREEN_ROUTE) {
                    FirstSetupScreen(
                        userSettingsRepository = userSettingsRepository,
                        navController = navController
                    )
                }
                composable(route = WELCOME_SCREEN_ROUTE) {
                    WelcomeScreen(
                        userSettingsRepository = userSettingsRepository,
                        navController = navController
                    )
                }
                composable(route = SIGNIN_SCREEN_ROUTE) {
                    SignInScreen(
                        firebaseAuth = firebaseAuth,
                        appContext = appContext,
                        navController = navController,
                        oneTapClient = oneTapClient
                    )
                }
                composable(route = LEADERBOARDS_ROUTE) {
                    LeaderboardScreen(
                        firestoreDatabase = firestoreDatabase,
                        firebaseAuth = firebaseAuth,
                        oneTapClient = oneTapClient,
                        navController = navController,
                        userSettingsRepository = userSettingsRepository
                    )
                }

            }
        }
    }
}

