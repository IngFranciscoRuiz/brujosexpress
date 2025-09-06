package com.fjapps.brujosexpress.admin.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Brand colors (fixed; no dynamic color)
private val Primary = Color(0xFF6E56CF) // Morado
private val PrimaryContainer = Color(0xFFE8E3FF)
private val Secondary = Color(0xFFFF8A00) // Naranja CTA
private val SecondaryContainer = Color(0xFFFFE2C4)
private val Background = Color(0xFFFFFFFF)
private val Surface = Color(0xFFFFFFFF)
private val OnSurface = Color(0xFF1B1B1B)
private val BrandError = Color(0xFFD32F2F)
@Suppress("unused")
private val BrandSuccess = Color(0xFF28B463)

private val AdminLightColors: ColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = Color.White,
    primaryContainer = PrimaryContainer,
    secondary = Secondary,
    onSecondary = Color.White,
    secondaryContainer = SecondaryContainer,
    background = Background,
    surface = Surface,
    onSurface = OnSurface,
    error = BrandError
)

private val AdminDarkColors: ColorScheme = darkColorScheme(
    primary = Primary,
    onPrimary = Color.White,
    primaryContainer = PrimaryContainer,
    secondary = Secondary,
    onSecondary = Color.White,
    secondaryContainer = SecondaryContainer,
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onSurface = Color(0xFFEAEAEA),
    error = BrandError
)

@Composable
fun AdminTheme(
    useDarkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    // Dynamic color explicitly disabled; we always use our fixed schemes
    val colors = if (useDarkTheme) AdminDarkColors else AdminLightColors
    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}


