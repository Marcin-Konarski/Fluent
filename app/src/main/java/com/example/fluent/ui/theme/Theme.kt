package com.example.fluent.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme // Dodaj ten import
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme // Nadal używamy lightColorScheme jako podstawy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.darkColorScheme

// Definicja motywów (enum)
enum class AppTheme(
    val displayName: String,
    val primaryColor: Color,
    val secondaryColor: Color,
    val tertiaryColor: Color,
    val quaternaryColor: Color
) {
    OCEAN_BLUE(
        "Ocean Blue",
        OceanBlue,
        NeonBlue,
        AquaBlue,
        SkyBlue
    ),
    NEON_PINK(
        "Neon Pink",
        CandyPink,
        NeonPink,
        CoralPink,
        BlushPink
    ),
    EMERALD_GREEN(
        "Emerald Green",
        EmeraldGreen,
        MintGreen,
        JadeGreen,
        SeafoamGreen
    ),
    ROYAL_PURPLE(
        "Royal Purple",
        PurpleVelvet,
        RoyalViolet,
        LightMagenta,
        OrchidMagenta
    ),
    MIDNIGHT_MYSTERY(
        "Midnight Mystery",
        MidnightBlue,
        CoolCyan,
        SkyTeal,
        DeepBlue
    ),
    SOFT_LAVENDER(
        "Soft Lavender",
        OrchidMagenta,
        BlushPink,
        LightMagenta,
        DeepMagenta
    ),
    DEEP_ROSE(
        "Deep Rose",
        DeepRose,
        HotPink,
        RaspberryPink,
        NeonPink
    ),
    MAGENTA(
        "Magenta",
        DeepMagenta,
        OrchidMagenta,
        VividMagenta,
        NeonMagenta
    )
}




@Composable
fun getThemeColorScheme(appTheme: AppTheme): ColorScheme {
    val isSystemDark = isSystemInDarkTheme()

    return if (isSystemDark) {
        darkColorScheme(
            primary = appTheme.primaryColor,
            secondary = appTheme.secondaryColor,
            tertiary = appTheme.tertiaryColor,
            background = Color(0xFF121212), // typowe tło dark mode
            surface = Color(0xFF121212),
            onPrimary = Color.White,
            onSecondary = Color.White,
            onTertiary = Color.White,
            onBackground = Color.White,
            onSurface = Color.White
        )
    } else {
        lightColorScheme(
            primary = appTheme.primaryColor,
            secondary = appTheme.secondaryColor,
            tertiary = appTheme.tertiaryColor,
            background = Color.White,
            surface = Color.White,
            onPrimary = Color.Black,
            onSecondary = Color.Black,
            onTertiary = Color.Black,
            onBackground = Color.Black,
            onSurface = Color.Black
        )
    }
}


// Lokalna kompozycja dla aktualnego motywu
val LocalAppTheme = staticCompositionLocalOf { AppTheme.MIDNIGHT_MYSTERY }

@Composable
fun FluentTheme(
    appTheme: AppTheme = LocalAppTheme.current,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalAppTheme provides appTheme) {
        MaterialTheme(
            colorScheme = getThemeColorScheme(appTheme), // Teraz getThemeColorScheme jest Composable
            content = content
        )
    }
}
