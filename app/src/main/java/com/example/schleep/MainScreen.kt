package com.example.schleep


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.schleep.components.BottomNavItem
import com.example.schleep.db.SleepRecordRepository
import com.example.schleep.db.UserSettingsRepository
import com.example.schleep.screens.HistoryScreen
import com.example.schleep.screens.HomeScreen
import com.example.schleep.screens.SettingsScreen
import com.example.schleep.screens.StatsScreen
import com.example.schleep.screens.TipsScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(userSettingsRepository: UserSettingsRepository, sleepRecordRepository: SleepRecordRepository) {
    val navController = rememberNavController()
    val backStackEntry = navController.currentBackStackEntryAsState()
    val screens = listOf(
        BottomNavItem.Home,
        BottomNavItem.History,
        BottomNavItem.Stats,
        BottomNavItem.Settings,
        BottomNavItem.Tips
    )

    Scaffold(
        topBar = {
            Box(contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxWidth()
                        .height(60.dp)
                        .background(color = MaterialTheme.colorScheme.surfaceVariant)) {
                    Text(text = "Schleep",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.displaySmall,
                        modifier = Modifier.padding(all = 3.dp).align(Alignment.Center)
                    )
            }
            /*CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
                title = { Text(textAlign = TextAlign.Center, text = "Schleep", fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onPrimary) },
                navigationIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.images),
                                contentDescription = "Logo",
                                modifier = Modifier.padding(all = 3.dp)


                            )
                        }
            )*/
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
            ) {
                screens.forEach { item ->
                    val selected = item.route == backStackEntry.value?.destination?.route
                    NavigationBarItem(
                        selected = selected,
                        label = {
                            Text(
                                text = item.name,
                                fontWeight = FontWeight.SemiBold,
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
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) {
        Box(modifier = Modifier.padding(it).padding(paddingValues = PaddingValues(all = 15.dp))) {
            NavHost(
                navController = navController,
                startDestination = BottomNavItem.Home.route
            ) {
                composable(route = BottomNavItem.Home.route) {
                    HomeScreen(sleepRecordRepository = sleepRecordRepository)
                }
                composable(route = BottomNavItem.History.route) {
                    HistoryScreen(sleepRecordRepository = sleepRecordRepository)
                }
                composable(route = BottomNavItem.Settings.route) {
                    SettingsScreen(userSettingsRepository = userSettingsRepository)
                }
                composable(route = BottomNavItem.Stats.route) {
                    StatsScreen(sleepRecordRepository = sleepRecordRepository)
                }
                composable(route = BottomNavItem.Tips.route) {
                    TipsScreen()
                }
            }
        }
    }
}

