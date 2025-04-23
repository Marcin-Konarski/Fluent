package com.example.fluent.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

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

// Funkcja generująca ColorScheme na podstawie AppTheme
fun getThemeColorScheme(appTheme: AppTheme): ColorScheme {
    return lightColorScheme(
        primary = appTheme.primaryColor,
        secondary = appTheme.secondaryColor,
        tertiary = appTheme.tertiaryColor,
        background = OffWhite,
        surface = OffWhite,
        onPrimary = OffWhite,
        onSecondary = OffWhite,
        onTertiary = OffWhite,
        onBackground = OffWhite,
        onSurface = OffWhite,
    )
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
            colorScheme = getThemeColorScheme(appTheme),
            content = content
        )
    }
}