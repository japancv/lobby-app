package com.example.lobbyapp.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.lobbyapp.R
import com.example.lobbyapp.ui.theme.LobbyAppTheme
import com.example.lobbyapp.ui.theme.body5
import com.example.lobbyapp.util.pixelToSecondaryDp

@Composable
fun ConfirmDialog(
    title: String,
    text: String,
    onConfirm: () -> Unit = {},
    onCancel: () -> Unit = {}
) {
    CustomDialog(
        confirmButton = {
            CustomButton(buttonText = stringResource(R.string.confirm), onClick = onConfirm)
        },
        dismissButton = {
            CustomButton(
                modifier = Modifier.padding(end = 8.pixelToSecondaryDp),
                buttonText = stringResource(R.string.cancel),
                background = Color.White,
                textColor = MaterialTheme.colors.secondaryVariant,
                border = BorderStroke(4.pixelToSecondaryDp, Color.LightGray),
                onClick = onCancel,
            )
        },
        title = { Text(text = title, style = MaterialTheme.typography.h4) },
        text = { Text(text = text, style = MaterialTheme.typography.body5) }
    )
}

@Preview(showBackground = true)
@Composable
fun ConfirmDialogPreview() {
    LobbyAppTheme {
        GlobalLayout {
            ConfirmDialog(text = "are you sure", title = "are you?")
        }
    }
}