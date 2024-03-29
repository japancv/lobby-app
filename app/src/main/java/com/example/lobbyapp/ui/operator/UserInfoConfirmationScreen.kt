package com.example.lobbyapp.ui.operator

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lobbyapp.R
import com.example.lobbyapp.ui.component.CustomButton
import com.example.lobbyapp.ui.component.ErrorDialog
import com.example.lobbyapp.ui.component.GlobalLayout
import com.example.lobbyapp.ui.theme.LobbyAppTheme
import com.example.lobbyapp.ui.viewModel.UserInfoViewModel
import com.example.lobbyapp.util.formatName
import com.example.lobbyapp.util.toSecondarySp
import kotlinx.coroutines.launch

@Composable
fun UserInfoConfirmationScreen(
    onCancelButtonClicked: () -> Unit = {},
    onContinueButtonClicked: () -> Unit = {},
    onEditButtonClicked: () -> Unit = {},
    onRetryButtonClicked: () -> Unit = {},
) {
    val coroutineScope = rememberCoroutineScope()
    val error = remember { mutableStateOf<String?>(null) }
    val userInfoUiState = UserInfoViewModel.uiState.collectAsState()

    if (error.value != null) {
        ErrorDialog(
            errorMessage = error.value,
            onRetryButtonClicked = {
                error.value = null
                onRetryButtonClicked()
            }
        )
    }

    GlobalLayout(headerBottomBorder = false, onCancelButtonClicked = onCancelButtonClicked) {
        Text(
            modifier = Modifier.padding(bottom = 36.dp),
            text = stringResource(R.string.confirm_info),
            style = MaterialTheme.typography.h3.toSecondarySp()
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            InfoRow(
                key = stringResource(R.string.name),
                value = formatName(
                    firstName = userInfoUiState.value.firstName,
                    lastName = userInfoUiState.value.lastName
                )
            )
            InfoRow(
                key = stringResource(R.string.phone_number),
                value = userInfoUiState.value.phoneNumber
            )
            InfoRow(
                key = stringResource(R.string.email),
                value = userInfoUiState.value.email
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 100.dp, end = 100.dp)
            ) {
                CustomButton(
                    buttonText = stringResource(R.string.edit),
                    background = MaterialTheme.colors.background,
                    textColor = MaterialTheme.colors.primary,
                    border = BorderStroke(1.dp, MaterialTheme.colors.secondary),
                    onClick = onEditButtonClicked,
                    modifier = Modifier.weight(1f)
                )
                CustomButton(
                    buttonText = stringResource(R.string.continues),
                    onClick = {
                        coroutineScope.launch {
                            UserInfoViewModel.createIdentity(
                                onSuccess = onContinueButtonClicked,
                                onError = {
                                    error.value = it
                                }
                            )
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 1080)
@Composable
fun UserInfoConfirmationScreenPreview() {
    LobbyAppTheme {
        UserInfoConfirmationScreen()
    }
}