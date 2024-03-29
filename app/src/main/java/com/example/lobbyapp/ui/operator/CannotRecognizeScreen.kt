package com.example.lobbyapp.ui.operator

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lobbyapp.R
import com.example.lobbyapp.ui.component.CustomButton
import com.example.lobbyapp.ui.component.GlobalLayout
import com.example.lobbyapp.ui.theme.LobbyAppTheme
import com.example.lobbyapp.util.pixelToSecondaryDp
import com.example.lobbyapp.util.toSecondarySp

@Composable
fun CannotRecognizeScreen(
    onCancelButtonClicked: () -> Unit = {},
    onHomeButtonClicked: () -> Unit = {},
    onRegisterButtonClicked: () -> Unit = {},
    onScanButtonClicked: () -> Unit = {}
) {
    GlobalLayout(
        isSecondaryDisplay = true,
        headerBottomBorder = false,
        onCancelButtonClicked = onCancelButtonClicked,
    ) {
        Text(
            stringResource(R.string.not_registered_found),
            style = MaterialTheme.typography.h1.toSecondarySp()
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 80.pixelToSecondaryDp, bottom = 40.pixelToSecondaryDp),
            horizontalArrangement = Arrangement.spacedBy(32.pixelToSecondaryDp)
        ) {
            CustomButton(
                modifier = Modifier.weight(1f),
                background = MaterialTheme.colors.background,
                border = BorderStroke(1.dp, Color.LightGray),
                onClick = onScanButtonClicked,
                buttonText = stringResource(R.string.scan),
                textColor = MaterialTheme.colors.primary,
            )
            CustomButton(
                modifier = Modifier.weight(1f),
                background = MaterialTheme.colors.background,
                border = BorderStroke(1.dp, Color.LightGray),
                onClick = onRegisterButtonClicked,
                buttonText = stringResource(R.string.register),
                textColor = MaterialTheme.colors.primary,
            )
        }
        CustomButton(
            modifier = Modifier.fillMaxWidth(0.5f),
            onClick = onHomeButtonClicked,
            buttonText = stringResource(R.string.home),
        )
    }
}

@Preview(showBackground = true, widthDp = 1080)
@Composable
fun CannotRecognizePreview() {
    LobbyAppTheme { CannotRecognizeScreen() }
}