package com.example.lobbyapp.ui.operator

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.lobbyapp.R
import com.example.lobbyapp.ui.component.CustomButton
import com.example.lobbyapp.ui.component.GlobalLayout
import com.example.lobbyapp.ui.theme.LobbyAppTheme
import com.example.lobbyapp.ui.theme.body5
import com.example.lobbyapp.util.pixelToSecondaryDp
import com.example.lobbyapp.util.toSecondarySp

@Composable
fun HomeScreen(
    onManageButtonClicked: () -> Unit = {},
    onCloseButtonClicked: () -> Unit = {},
) {
    GlobalLayout(isSecondaryDisplay = true, headerButtonText = "") {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(60.pixelToSecondaryDp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomButton(
                buttonText = stringResource(R.string.manage_grant_access),
                modifier = Modifier
                    .height(200.pixelToSecondaryDp)
                    .width(700.pixelToSecondaryDp),
                background = MaterialTheme.colors.background,
                textColor = MaterialTheme.colors.secondaryVariant,
                textTypo = MaterialTheme.typography.h1.toSecondarySp(),
                border = BorderStroke(6.pixelToSecondaryDp, Color.LightGray),
                onClick = onManageButtonClicked,
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 1080)
@Composable
fun OperatorHomeScreenPreview() {
    LobbyAppTheme {
        HomeScreen()
    }
}