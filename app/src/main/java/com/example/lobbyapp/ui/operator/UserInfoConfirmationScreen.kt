package com.example.lobbyapp.ui.operator

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
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
import com.example.lobbyapp.util.toSecondarySp
import kotlinx.coroutines.launch

@Composable
fun UserInfoConfirmationScreen(
    onCancelButtonClicked: () -> Unit = {},
    onContinueButtonClicked: () -> Unit = {},
    onEditButtonClicked: () -> Unit = {}
) {
    val coroutineScope = rememberCoroutineScope()
    val showErrorDialog = remember { mutableStateOf(false) }
    val userInfoUiState = UserInfoViewModel.uiState.collectAsState()

    GlobalLayout(headerBottomBorder = false, onCancelButtonClicked = onCancelButtonClicked) {
        if (showErrorDialog.value) {
            ErrorDialog(onConfirm = {
                showErrorDialog.value = false
            })
        }

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
                value = "${userInfoUiState.value.firstName} ${userInfoUiState.value.lastName}"
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
                                    showErrorDialog.value = true
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