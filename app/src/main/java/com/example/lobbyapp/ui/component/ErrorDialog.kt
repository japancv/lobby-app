package com.example.lobbyapp.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lobbyapp.R
import com.example.lobbyapp.ui.theme.LobbyAppTheme
import com.example.lobbyapp.ui.theme.body5

@Composable
fun ErrorDialog(
    errorMessage: String = stringResource(R.string.default_error_message),
    onConfirm: () -> Unit = {}
) {
    AlertDialog(
        onDismissRequest = { },
        confirmButton = {
            CustomButton(
                modifier = Modifier.padding(end = 8.dp, bottom = 8.dp),
                buttonText = stringResource(R.string.retry),
                textTypo = MaterialTheme.typography.body5,
                onClick = onConfirm
            )
        },
        title = {
            Text(
                text = stringResource(R.string.error),
                style = MaterialTheme.typography.h4
            )
        },
        text = {
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.body5
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ErrorDialogPreview() {
    LobbyAppTheme {
        GlobalLayout {
            ErrorDialog()
        }
    }
}