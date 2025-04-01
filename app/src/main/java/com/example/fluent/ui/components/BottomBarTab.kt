package com.example.fluent.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarTab(val title: String, val icon: ImageVector, val color: Color) {
    object Words : BottomBarTab(
        title = "Words",
        icon = Icons.Rounded.Home,
        color = Color(0xFFFA6FFF)
    )
    object Learn : BottomBarTab(
        title = "Learn",
        icon = Icons.Rounded.Person,
        color = Color(0xFFFFA574)
    )
    object FlashCards : BottomBarTab(
        title = "Cards",
        icon = Icons.Rounded.Settings,
        color = Color(0xFFADFF64)
    )
}

val tabs = listOf(
    BottomBarTab.Words,
    BottomBarTab.Learn,
    BottomBarTab.FlashCards,
)