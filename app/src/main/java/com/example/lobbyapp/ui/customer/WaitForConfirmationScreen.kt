package com.example.lobbyapp.ui.customer

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lobbyapp.R
import com.example.lobbyapp.ui.component.GlobalLayout
import com.example.lobbyapp.ui.theme.LobbyAppTheme
import com.example.lobbyapp.ui.theme.body5
import com.example.lobbyapp.ui.viewModel.UserInfoViewModel

@Composable
fun WaitForConfirmationScreen(
    onCancelButtonClicked: () -> Unit = {},
) {
    val userInfoUiState = UserInfoViewModel.uiState.collectAsState()

    GlobalLayout(onCancelButtonClicked = onCancelButtonClicked) {
        Text(
            "${userInfoUiState.value.firstName} ${userInfoUiState.value.lastName}",
            style = MaterialTheme.typography.h3,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = if (userInfoUiState.value.isRegister) stringResource(R.string.welcome) else stringResource(
                R.string.welcome_back
            ),
            style = MaterialTheme.typography.h1
        )
        Spacer(modifier = Modifier.height(28.dp))
        Text(
            stringResource(R.string.wait_for_confirmation),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.body5,
            color = MaterialTheme.colors.secondary
        )
    }
}


@Preview(showBackground = true)
@Composable
fun WaitForConfirmationPreview() {
    LobbyAppTheme {
        WaitForConfirmationScreen()
    }
}