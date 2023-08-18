package com.example.schleep.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val name: String,
    val route: String,
    val icon: ImageVector,
) {
    object Home : BottomNavItem(
        name = "Budík",
        route = "home",
        icon = Icons.Rounded.Home
    )

    object History : BottomNavItem(
        name = "Historie",
        route = "history",
        icon = Icons.Rounded.List
    )

    object Settings : BottomNavItem(
        name = "Nastavení",
        route = "settings",
        icon = Icons.Rounded.Settings
    )

    object Stats : BottomNavItem(
        name = "Statistiky",
        route = "stats",
        icon = Icons.Rounded.DateRange
    )

    object Tips : BottomNavItem(
        name = "Tipy",
        route = "tips",
        icon = Icons.Rounded.Info
    )
}
