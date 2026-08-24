package com.focusfera.app.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = FocusBlue,
    secondary = FocusGreen,
    background = DarkBackground,
    surface = DarkSurface,
    onPrimary = DarkText,
    onSecondary = DarkText,
    onBackground = DarkText,
    onSurface = DarkText
)

private val LightColorScheme = lightColorScheme(
    primary = FocusBlue,
    secondary = FocusGreen,
    background = LightBackground,
    surface = LightSurface,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    onSecondary = androidx.compose.ui.graphics.Color.White,
    onBackground = LightText,
    onSurface = LightText
)

@Composable
fun FocusferaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    val view = LocalView.current

    if (!view.isInEditMode) {
        val window = (view.context as Activity).window

        window.statusBarColor = colorScheme.background.toArgb()

        WindowCompat.getInsetsController(
            window,
            view
        ).isAppearanceLightStatusBars = !darkTheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}