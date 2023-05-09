package com.example.lobbyapp.ui.component

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.lobbyapp.R
import com.example.lobbyapp.ui.theme.LobbyAppTheme
import com.example.lobbyapp.ui.theme.body5
import com.example.lobbyapp.util.getStringResource

@Composable
fun ErrorDialog(
    errorMessage: String? = null,
    onRetryButtonClicked: () -> Unit = {}
) {
    CustomDialog(
        confirmButton = {
            CustomButton(
                buttonText = stringResource(R.string.retry),
                textTypo = MaterialTheme.typography.body5,
                onClick = onRetryButtonClicked
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
                text = if (errorMessage.isNullOrBlank()) stringResource(R.string.default_error_message) else {
                    getStringResource(LocalContext.current, errorMessage) ?: errorMessage
                },
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