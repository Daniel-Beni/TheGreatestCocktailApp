package fr.isen.danielbeni.thegreatestcocktailapp.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * je  force le ColorScheme.
 */

private val DarkColorScheme = darkColorScheme(
    primary = GoldAccent,
    secondary = WinePlum,
    tertiary = WineLight,
    background = WineDark,
    surface = Color(0xFF211D2B),         // surface légèrement plus clair que le background
    onPrimary = WineDark,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = WinePlum,
    secondary = GoldAccent,
    tertiary = WineRose,
    background = Color(0xFFF8F5F2),      // Crème très léger
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = WineDark,
    onBackground = WineDark,
    onSurface = WineDark
)

@Composable
fun TheGreatestCocktailAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // Pas de dynamicColor → on impose la palette Bacchus
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}