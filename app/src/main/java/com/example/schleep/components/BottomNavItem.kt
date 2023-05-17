package com.example.schleep.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val name: String,
    val route: String,
    val icon: ImageVector,
) {
    object Home: BottomNavItem (
        name = "Home",
        route = "home",
        icon = Icons.Rounded.Home
    )
    object History: BottomNavItem (
        name = "History",
        route = "history",
        icon = Icons.Rounded.List
    )
    object Settings: BottomNavItem (
        name = "Settings",
        route = "settings",
        icon = Icons.Rounded.Settings
    )
    object Stats: BottomNavItem (
        name = "Statistics",
        route = "stats",
        icon = Icons.Rounded.DateRange
    )
    object Tips: BottomNavItem (
        name = "Tipy",
        route = "tips",
        icon = Icons.Rounded.Info
    )
}
