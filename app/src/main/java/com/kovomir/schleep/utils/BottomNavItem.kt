package com.kovomir.schleep.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val name: String,
    val route: String,
    val icon: ImageVector,
) {
    object Home : BottomNavItem(
        name = "Spánek",
        route = "home",
        icon = Icons.Rounded.PlayArrow
    )

    object History : BottomNavItem(
        name = "Historie",
        route = "history",
        icon = Icons.Rounded.DateRange
    )

    object Settings : BottomNavItem(
        name = "Nastavení",
        route = "settings",
        icon = Icons.Rounded.Settings
    )

    object Stats : BottomNavItem(
        name = "Statistiky",
        route = "stats",
        icon = Icons.Rounded.List
    )

    object Tips : BottomNavItem(
        name = "Tipy",
        route = "tips",
        icon = Icons.Rounded.Info
    )
}
