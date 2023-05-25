package com.example.lobbyapp.ui.operator

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.lobbyapp.R
import com.example.lobbyapp.ui.component.CustomButton
import com.example.lobbyapp.ui.component.GlobalLayout
import com.example.lobbyapp.ui.theme.LobbyAppTheme
import com.example.lobbyapp.ui.viewModel.UserInfoViewModel
import com.example.lobbyapp.util.formatName
import com.example.lobbyapp.util.pixelToSecondaryDp
import com.example.lobbyapp.util.toSecondarySp

@Composable
fun WelcomeScreen(
    onGoToNextScreen: () -> Unit = {},
    onCancelButtonClicked: () -> Unit = {},
) {
    val userInfoUiState = UserInfoViewModel.uiState.collectAsState()

    GlobalLayout(
        headerBottomBorder = false,
        headerButtonText = "End",
        onCancelButtonClicked = onCancelButtonClicked
    ) {
        Text(
            text = "${stringResource(R.string.welcome)} ${
                formatName(
                    firstName = userInfoUiState.value.firstName,
                    lastName = userInfoUiState.value.lastName
                )
            }",
            style = MaterialTheme.typography.h1.toSecondarySp()
        )
        CustomButton(
            modifier = Modifier
                .padding(top = 80.pixelToSecondaryDp)
                .fillMaxWidth(0.25f),
            buttonText = stringResource(R.string.register),
            onClick = onGoToNextScreen
        )
    }
}

@Preview(showBackground = true, widthDp = 1080)
@Composable
fun WelcomeScreenPreview() {
    LobbyAppTheme { WelcomeScreen() }
}
