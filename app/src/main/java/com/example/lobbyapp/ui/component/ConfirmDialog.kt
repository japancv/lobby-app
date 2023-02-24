package com.example.lobbyapp.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.lobbyapp.R
import com.example.lobbyapp.ui.theme.LobbyAppTheme
import com.example.lobbyapp.util.pixelToSecondaryDp

@Composable
fun ConfirmDialog(
    title: String,
    text: String,
    onConfirm: () -> Unit = {},
    onCancel: () -> Unit = {}
) {
    AlertDialog(
        onDismissRequest = {},
        confirmButton = {
            CustomButton(buttonText = stringResource(R.string.confirm), onClick = onConfirm)
        },
        dismissButton = {
            CustomButton(
                buttonText = stringResource(R.string.cancel),
                background = Color.White,
                textColor = MaterialTheme.colors.secondaryVariant,
                border = BorderStroke(6.pixelToSecondaryDp, Color.LightGray),
                onClick = onCancel,
            )
        },
        title = { Text(text = title, color = MaterialTheme.colors.secondaryVariant) },
        text = { Text(text = text, color = MaterialTheme.colors.secondaryVariant) }
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