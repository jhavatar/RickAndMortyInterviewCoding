package io.chthonic.rickmortychars.presentation.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColors = darkColors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondary = Teal200,
    secondaryVariant = Teal200,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onSurface = Color.Black,
    surface = Purple200 // top app bar
)
private val LightColors = lightColors(
    primary = Purple500, // top app bar
    primaryVariant = Purple700,
    secondary = Teal200,
    secondaryVariant = Teal700,
    onPrimary = Color.White,
    onSecondary = Color.Black
)

@Composable
fun AppTheme(
    isDarkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = if (isDarkTheme) DarkColors else LightColors,
        content = content
    )
}