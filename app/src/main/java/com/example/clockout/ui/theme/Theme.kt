package com.example.clockout.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
// Primary
    primary = DarkOrange,
    onPrimary = Color.White,
    primaryContainer =  Color.White.copy(alpha = .9f),
    onPrimaryContainer = Color.Black,
// Secondary
// Tertiary: Simple App no need for multiple colors - same as primary for TimePicker
    tertiaryContainer =  DarkOrange,
    onTertiaryContainer = Color.White,
    background = DarkOrange,
// Surface
    surface = Color.White,
    surfaceVariant = Color.White, // Card BG
    onSurface = Color.Black,
)

private val DarkColorScheme = darkColorScheme(
// Primary
    primary = DarkOrange,
    onPrimary = VeryDarkGray,
    primaryContainer = VeryDarkGray.copy(alpha = .9f),
    onPrimaryContainer = LightGray,
// Secondary
// Tertiary: Simple App no need for multiple colors - same as primary for TimePicker
    tertiaryContainer =  DarkOrange,
    onTertiaryContainer = VeryDarkGray,
// Background
    background = AlmostBlack,

// Surface
    surface = VeryDarkGray,
    onSurface = LightGray,
    surfaceTint = LightGray,
    surfaceVariant = VeryDarkGray

)

@Composable
fun ClockoutTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true, content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme, typography = Typography, content = content
    )
}
