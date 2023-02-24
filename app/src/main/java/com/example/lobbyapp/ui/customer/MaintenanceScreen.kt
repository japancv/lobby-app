package com.example.lobbyapp.ui.customer

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.lobbyapp.R
import com.example.lobbyapp.ui.component.GlobalLayout
import com.example.lobbyapp.ui.theme.LobbyAppTheme

@Composable
fun MaintenanceScreen() {
    GlobalLayout(headerButtonText = "") {
        Text(
            text = stringResource(id = R.string.under_maintenance),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h1
        )
    }
}


@Preview(showBackground = true)
@Composable
fun MaintenancePreview() {
    LobbyAppTheme {
        MaintenanceScreen()
    }
}