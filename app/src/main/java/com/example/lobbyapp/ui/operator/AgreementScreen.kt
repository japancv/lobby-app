package com.example.lobbyapp.ui.operator

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.lobbyapp.R
import com.example.lobbyapp.ui.component.GlobalLayout
import com.example.lobbyapp.ui.theme.LobbyAppTheme
import com.example.lobbyapp.util.toSecondarySp

@Composable
fun AgreementScreen(
    onCancelButtonClicked: () -> Unit = {},
) {
    GlobalLayout(
        isSecondaryDisplay = true,
        headerBottomBorder = false,
        onCancelButtonClicked = onCancelButtonClicked,
    ) {
        Text(
            stringResource(R.string.wait_for_agreement_confirmation),
            style = MaterialTheme.typography.h1.toSecondarySp()
        )
    }
}

@Preview(showBackground = true, widthDp = 1080)
@Composable
fun AgreementScreensPreview() {
    LobbyAppTheme { AgreementScreen() }
}