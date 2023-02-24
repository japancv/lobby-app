package com.example.lobbyapp.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lobbyapp.ui.theme.LobbyAppTheme


@Composable
fun CustomButton(
    buttonText: String,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    disabled: Boolean = false,
    background: Color = MaterialTheme.colors.primary,
    textColor: Color = Color.White,
    textTypo: TextStyle = MaterialTheme.typography.h4,
    border: BorderStroke = BorderStroke(0.dp, Color.White),
    fontWeight: FontWeight = FontWeight.W600,
    shape: Shape = RoundedCornerShape(12.dp),
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = background,
        ),
        enabled = !disabled,
        border = border,
        elevation = ButtonDefaults.elevation(0.dp, 8.dp, 0.dp),
        modifier = modifier,
        shape = shape
    ) {
        Text(
            buttonText,
            modifier = Modifier.padding(4.dp),
            color = textColor,
            style = textTypo,
            fontWeight = fontWeight
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CustomButtonPreview() {
    LobbyAppTheme {
        CustomButton(
            buttonText = "TEST"
        )
    }
}