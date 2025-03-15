package com.example.fluent.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.foundation.isSystemInDarkTheme

private val LightColors = lightColorScheme(
    primary = androidx.compose.ui.graphics.Color(0x666200EE),
    secondary = androidx.compose.ui.graphics.Color(0x6603DAC6)
    // You can customize other colors as needed
)

private val DarkColors = darkColorScheme(
    primary = androidx.compose.ui.graphics.Color(0x66BB86FC),
    secondary = androidx.compose.ui.graphics.Color(0x6603DAC6)
    // You can customize other colors as needed
)

@Composable
fun FluentTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColors
    } else {
        LightColors
    }

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}