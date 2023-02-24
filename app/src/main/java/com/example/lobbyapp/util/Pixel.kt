package com.example.lobbyapp.util

import androidx.compose.runtime.Stable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.*

const val SECONDARY_SCREEN_DENSITY = 1.33125f

@Stable
inline val Int.pixelToSecondaryDp: Dp
    get() = Dp(value = this.toFloat() / SECONDARY_SCREEN_DENSITY)

@Stable
inline val Float.pixelToSecondaryDp: Dp
    get() = Dp(value = this / SECONDARY_SCREEN_DENSITY)


@OptIn(ExperimentalUnitApi::class)
@Stable
val Float.pixelToSecondarySp: TextUnit
    get() = TextUnit(this / SECONDARY_SCREEN_DENSITY, TextUnitType.Sp)

@OptIn(ExperimentalUnitApi::class)
@Stable
val Int.pixelToSecondarySp: TextUnit
    get() = TextUnit(this / SECONDARY_SCREEN_DENSITY, TextUnitType.Sp)

@Stable
fun TextStyle.toSecondarySp(): TextStyle {
    return copy(
        fontSize = fontSize.value.toInt().pixelToSecondarySp,
        lineHeight = lineHeight.value.toInt().pixelToSecondarySp
    )
}
