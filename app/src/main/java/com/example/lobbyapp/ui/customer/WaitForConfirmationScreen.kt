package com.example.lobbyapp.ui.customer

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.lobbyapp.R
import com.example.lobbyapp.ui.component.GlobalLayout
import com.example.lobbyapp.ui.theme.body5
import com.example.lobbyapp.ui.viewModel.UserInfoViewModel
import com.example.lobbyapp.util.formatName

@Composable
fun WaitForConfirmationScreen(
    onCancelButtonClicked: () -> Unit = {},
) {
    val userInfoUiState = UserInfoViewModel.uiState.collectAsState()
    val welcomeText = if (userInfoUiState.value.isRegister) {
        stringResource(R.string.welcome)
    } else {
        if (userInfoUiState.value.qrCodeScanned) {
            stringResource(
                R.string.register_success
            )
        } else {
            stringResource(
                R.string.welcome_back
            )
        }
    }

    GlobalLayout(onCancelButtonClicked = onCancelButtonClicked) {
        Text(
            text = formatName(
                firstName = userInfoUiState.value.firstName,
                lastName = userInfoUiState.value.lastName,
                addSaMa = true
            ),
            modifier = Modifier.padding(bottom = 12.dp),
            style = MaterialTheme.typography.h3,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
        Text(
            text = welcomeText,
            modifier = Modifier.padding(bottom = 28.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h2
        )
        Text(
            stringResource(R.string.wait_for_confirmation),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.body5,
            color = MaterialTheme.colors.secondary
        )
    }
}
