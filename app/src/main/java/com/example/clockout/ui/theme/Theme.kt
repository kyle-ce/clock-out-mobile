package com.example.clockout.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = HomeDepotOrange,
    onPrimary = Color.White,
    primaryContainer = LightBeige,
    onPrimaryContainer = DarkBrown,
    secondary = LightBrown,
    onSecondary = Color.White,
    secondaryContainer = LightBeige,
    onSecondaryContainer = DarkBrown,
    tertiary = DarkBrown,
    onTertiary = Color.White,
    tertiaryContainer = LightBeige,
    onTertiaryContainer = DarkBrown,
    background = LightBeige,
    onBackground = DarkBrown,
    surface = LightBeige,
    onSurface = DarkBrown,
    error = Color.Red,
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD4),
    onErrorContainer = Color(0xFF410E0B),
    inversePrimary = LightBeige,
    inverseSurface = DarkBrown,
    inverseOnSurface = LightBeige,
    outline = DarkBrown,
    outlineVariant = LightBrown,
    surfaceTint = Color.Transparent,
    surfaceVariant = LightBeige
)

private val DarkColorScheme = darkColorScheme(
    primary = DarkOrange,
    onPrimary = Color.White,
    primaryContainer = MediumDarkOrange,
    onPrimaryContainer = Color.Black,
    secondary = SiennaBrown,
    onSecondary = Color.White,
    secondaryContainer = SaddleBrown,
    onSecondaryContainer = Color.White,
    tertiary = LightSalmon,
    onTertiary = Color.Black,
    tertiaryContainer = IndianRed,
    onTertiaryContainer = Color.White,
    background = AlmostBlack,
    onBackground = Color.Red,
    surface = VeryDarkGray,
    onSurface = LightGray,
    error = PinkishRed,
    onError = Color.White,
    errorContainer = DarkRed,
    onErrorContainer = Color.White,
    inversePrimary = MediumDarkOrange,
    inverseSurface = LightGray,
    inverseOnSurface = VeryDarkGray,
    outline = Gray,
    outlineVariant = SiennaBrown,
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
