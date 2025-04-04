package com.example.fluent.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircleOutline
import androidx.compose.material.icons.rounded.AutoAwesomeMotion
import androidx.compose.material.icons.rounded.BorderColor
import androidx.compose.material.icons.rounded.FormatListNumbered
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BarTabs(val title: String, val icon: ImageVector, val color: Color) {
    object FlashCards : BarTabs(
        title = "Cards",
        icon = Icons.Rounded.AutoAwesomeMotion, // .DynamicFeed,
        color = Color(0xFF00FF08)
    )
    object Words : BarTabs(
        title = "Words",
        icon = Icons.Rounded.FormatListNumbered,
        color = Color(0xFF00BCD4)
    )
    object AddWord : BarTabs(
        title = "Add Word",
        icon = Icons.Rounded.AddCircleOutline,
        color = Color(0xFFFF255E)
    )
    object Learn : BarTabs(
        title = "Learn",
        icon = Icons.Rounded.BorderColor, // .Draw,
        color = Color(0xFFEF613B)
    )
    object Settings : BarTabs(
        title = "Settings",
        icon = Icons.Rounded.Settings,
        color = Color(0xFFEAD852)
    )
}

val tabs = listOf(
    BarTabs.FlashCards,
    BarTabs.Words,
    BarTabs.AddWord,
    BarTabs.Learn,
    BarTabs.Settings,
)