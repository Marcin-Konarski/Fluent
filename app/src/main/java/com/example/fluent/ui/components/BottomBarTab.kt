package com.example.fluent.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarTab(val title: String, val icon: ImageVector, val color: Color) {
    object FlashCards : BottomBarTab(
        title = "Flash Cards",
        icon = Icons.Rounded.Settings,
        color = Color(0xFF009688)
    )
    object Words : BottomBarTab(
        title = "Words List",
        icon = Icons.Rounded.Home,
        color = Color(0xFFFA6FFF)
    )
    object Learn : BottomBarTab(
        title = "Learn Words",
        icon = Icons.Rounded.Person,
        color = Color(0xFFFFA574)
    )
    object AddWord : BottomBarTab(
        title = "Add Word",
        icon = Icons.Rounded.Settings,
        color = Color(0xFF00BCD4)
    )
    object Settings : BottomBarTab(
        title = "Settings",
        icon = Icons.Rounded.Settings,
        color = Color(0xFF673AB7)
    )


}

val tabs = listOf(
    BottomBarTab.Words,
    BottomBarTab.Learn,
    BottomBarTab.FlashCards,
)