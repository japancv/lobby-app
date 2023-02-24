package com.example.lobbyapp.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Typography.body3: TextStyle
    @Composable
    get() {
        return TextStyle(
            fontWeight = FontWeight.W400,
            fontSize = 32.sp,
            color = MaterialTheme.colors.secondaryVariant
        )
    }

val Typography.body4: TextStyle
    @Composable
    get() {
        return TextStyle(
            fontWeight = FontWeight.W400,
            fontSize = 26.sp,
            color = MaterialTheme.colors.secondaryVariant
        )
    }

val Typography.body4Bold: TextStyle
    @Composable
    get() {
        return TextStyle(
            fontWeight = FontWeight.W600,
            fontSize = 26.sp,
            color = MaterialTheme.colors.secondaryVariant
        )
    }

val Typography.body5: TextStyle
    @Composable
    get() {
        return TextStyle(
            fontWeight = FontWeight.W400,
            fontSize = 20.sp,
            color = MaterialTheme.colors.secondaryVariant
        )
    }

val Typography.body6: TextStyle
    @Composable
    get() {
        return TextStyle(
            fontWeight = FontWeight.W400,
            fontSize = 16.sp,
            color = MaterialTheme.colors.secondaryVariant
        )
    }

@Composable
fun Typography(textColor: Color): Typography {
    return Typography(
        h1 = TextStyle(
            fontWeight = FontWeight.W700,
            fontSize = 50.sp,
            color = textColor
        ),
        h2 = TextStyle(
            fontWeight = FontWeight.W700,
            fontSize = 40.sp,
            color = textColor
        ),
        h3 = TextStyle(
            fontWeight = FontWeight.W600,
            fontSize = 32.sp,
            color = textColor
        ),
        h4 = TextStyle(
            fontWeight = FontWeight.W600,
            fontSize = 26.sp,
            color = textColor
        ),
        h5 = TextStyle(
            fontWeight = FontWeight.W600,
            fontSize = 20.sp,
            color = textColor
        )
    )
}



