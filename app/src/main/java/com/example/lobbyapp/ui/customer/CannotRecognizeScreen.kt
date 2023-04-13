package com.example.lobbyapp.ui.customer

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lobbyapp.R
import com.example.lobbyapp.ui.component.GlobalLayout
import com.example.lobbyapp.ui.theme.LobbyAppTheme
import com.example.lobbyapp.ui.theme.body5
import com.example.lobbyapp.ui.theme.body6

@Composable
fun CannotRecognizeScreen(
    onCancelButtonClicked: () -> Unit = {},
    onScanButtonClicked: () -> Unit = {},
    onRegisterButtonClicked: () -> Unit = {}
) {
    GlobalLayout(
        onCancelButtonClicked = onCancelButtonClicked
    ) {
        Image(
            painter = painterResource(R.drawable.question),
            contentDescription = "question",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxWidth(0.5f),
        )
        Text(
            text = stringResource(R.string.not_registered_found),
            modifier = Modifier.padding(top = 36.dp, bottom = 20.dp),
            style = MaterialTheme.typography.h4,
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier.padding(bottom = 40.dp),
            text = stringResource(R.string.not_registered_found_hint),
            style = MaterialTheme.typography.body6,
            textAlign = TextAlign.Center
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = stringResource(R.string.have_a_qr_code),
                style = MaterialTheme.typography.body6
            )
            TextButton(onClick = onScanButtonClicked) {
                Text(
                    text = stringResource(R.string.scan),
                    color = MaterialTheme.colors.primary,
                    style = MaterialTheme.typography.body5
                )
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = stringResource(R.string.first_time_register_question),
                style = MaterialTheme.typography.body6
            )
            TextButton(onClick = onRegisterButtonClicked) {
                Text(
                    text = stringResource(R.string.register),
                    color = MaterialTheme.colors.primary,
                    style = MaterialTheme.typography.body5
                )
            }
        }
    }
}

@Preview
@Composable
fun CannotRecognizePreview() {
    LobbyAppTheme { CannotRecognizeScreen() }
}

