package com.example.kart.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Turquoise80,
    onPrimary = androidx.compose.ui.graphics.Color(0xFF003730),
    primaryContainer = androidx.compose.ui.graphics.Color(0xFF005047),
    onPrimaryContainer = androidx.compose.ui.graphics.Color(0xFF7CF8E6),

    secondary = Teal80,
    onSecondary = androidx.compose.ui.graphics.Color(0xFF003730),
    secondaryContainer = androidx.compose.ui.graphics.Color(0xFF005047),
    onSecondaryContainer = androidx.compose.ui.graphics.Color(0xFF7CF8E6),

    tertiary = Aqua80,
    onTertiary = androidx.compose.ui.graphics.Color(0xFF003730),
    tertiaryContainer = androidx.compose.ui.graphics.Color(0xFF005047),
    onTertiaryContainer = androidx.compose.ui.graphics.Color(0xFF7CF8E6),
    
    surface = androidx.compose.ui.graphics.Color(0xFF191C1C),
    onSurface = androidx.compose.ui.graphics.Color(0xFFE0E3E2),
    surfaceVariant = androidx.compose.ui.graphics.Color(0xFF3F4947),
    onSurfaceVariant = androidx.compose.ui.graphics.Color(0xFFBFC9C7),
    background = androidx.compose.ui.graphics.Color(0xFF191C1C),
    onBackground = androidx.compose.ui.graphics.Color(0xFFE0E3E2),
)

private val LightColorScheme = lightColorScheme(
    primary = Turquoise40,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    primaryContainer = androidx.compose.ui.graphics.Color(0xFFB2DFDB), // Light Turquoise
    onPrimaryContainer = androidx.compose.ui.graphics.Color(0xFF004D40), // Dark Turquoise
    
    secondary = Teal40,
    onSecondary = androidx.compose.ui.graphics.Color.White,
    secondaryContainer = androidx.compose.ui.graphics.Color(0xFFB2EBF2), // Light Teal
    onSecondaryContainer = androidx.compose.ui.graphics.Color(0xFF006064), // Dark Teal
    
    tertiary = Aqua40,
    onTertiary = androidx.compose.ui.graphics.Color.White,
    tertiaryContainer = androidx.compose.ui.graphics.Color(0xFF84FFFF),
    onTertiaryContainer = androidx.compose.ui.graphics.Color(0xFF006064),
    
    surface = androidx.compose.ui.graphics.Color(0xFFFBFDFD),
    onSurface = androidx.compose.ui.graphics.Color(0xFF191C1C),
    surfaceVariant = androidx.compose.ui.graphics.Color(0xFFDBE5E3),
    onSurfaceVariant = androidx.compose.ui.graphics.Color(0xFF3F4947),
    background = androidx.compose.ui.graphics.Color(0xFFFBFDFD),
    onBackground = androidx.compose.ui.graphics.Color(0xFF191C1C),

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
  )

@Composable
fun KartTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Dynamic color disabled so turquoise is always visible
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }
      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
