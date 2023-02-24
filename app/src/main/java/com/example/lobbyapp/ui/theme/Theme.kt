package com.example.lobbyapp.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

class Colors(
    primaryColor: String = "#C91320",
    secondaryColor: String = "#878787",
    backgroundColor: String = "#FFFFFF",
    textColor: String = "#363636"
) {
    val primaryColor: Color = Color(convertHexToArgb(primaryColor))
    val secondaryColor: Color = Color(convertHexToArgb(secondaryColor))
    val backgroundColor: Color = Color(convertHexToArgb(backgroundColor))
    val textColor: Color = Color(convertHexToArgb(textColor))
}

private val defaultColors = Colors()

private fun convertHexToArgb(hexColor: String): Long {
    return ("FF" + hexColor.substring(1)).toLong(16)
}

@Composable
fun LobbyAppTheme(colors: Colors = defaultColors, content: @Composable () -> Unit) {
    MaterialTheme(
        colors = lightColors(
            primary = colors.primaryColor,
            primaryVariant = colors.primaryColor,
            secondary = colors.secondaryColor,
            secondaryVariant = colors.textColor,
            background = colors.backgroundColor
        ),
        typography = Typography(colors.textColor),
        shapes = Shapes,
        content = content
    )
}