package com.nawa.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val NawaColors = darkColorScheme(
    primary = Gold,
    onPrimary = DeepInk,
    primaryContainer = GoldDeep,
    onPrimaryContainer = Ivory,
    secondary = SoftTeal,
    onSecondary = DeepInk,
    background = NightGreen,
    onBackground = Ivory,
    surface = SurfaceDark,
    onSurface = Ivory,
    surfaceVariant = SurfaceDark,
    onSurfaceVariant = Ivory
)

@Composable
fun NawaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = NawaColors,
        typography = NawaTypography,
        content = content
    )
}
