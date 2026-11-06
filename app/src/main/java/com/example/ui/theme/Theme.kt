package com.example.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = OfficeWordBlue,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFDCEAF9),
    onPrimaryContainer = OfficeWordDarkBlue,
    secondary = OfficeWordLightBlue,
    onSecondary = Color.White,
    secondaryContainer = OfficeRibbonItemActive,
    onSecondaryContainer = OfficeWordDarkBlue,
    tertiary = Color(0xFF2B579A),
    onTertiary = Color.White,
    background = OfficeDeskGray,
    onBackground = Color(0xFF201F1E),
    surface = Color.White,
    onSurface = Color(0xFF201F1E),
    surfaceVariant = OfficeRibbonBg,
    onSurfaceVariant = Color(0xFF605E5C),
    outline = OfficeRibbonBorder,
    outlineVariant = OfficeRibbonGroupBorder
)

private val DarkColorScheme = darkColorScheme(
    primary = OfficeWordLightBlue,
    onPrimary = Color.White,
    primaryContainer = OfficeWordDarkBlue,
    onPrimaryContainer = Color(0xFFDCEAF9),
    secondary = OfficeWordBlue,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF23354C),
    onSecondaryContainer = Color(0xFFDCEAF9),
    tertiary = Color(0xFF6BA5ED),
    onTertiary = Color.Black,
    background = OfficeDeskDarkGray,
    onBackground = Color(0xFFF3F2F1),
    surface = Color(0xFF29313D),
    onSurface = Color(0xFFF3F2F1),
    surfaceVariant = Color(0xFF1E2530),
    onSurfaceVariant = Color(0xFFA19F9D),
    outline = Color(0xFF484644),
    outlineVariant = Color(0xFF323130)
)

@Composable
fun WordProcessorTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep desktop Word authentic palette
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = OfficeWordBlue.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
