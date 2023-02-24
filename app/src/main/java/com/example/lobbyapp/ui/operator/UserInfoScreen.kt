package com.example.lobbyapp.ui.operator

import androidx.compose.foundation.layout.*
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.lobbyapp.R
import com.example.lobbyapp.ui.component.CustomButton
import com.example.lobbyapp.ui.component.GlobalLayout
import com.example.lobbyapp.ui.customer.ErrorMessageText
import com.example.lobbyapp.ui.theme.LobbyAppTheme
import com.example.lobbyapp.ui.viewModel.UserInfoViewModel
import com.example.lobbyapp.util.pixelToSecondaryDp

@Composable
fun UserInfoScreen(
    onCancelButtonClicked: () -> Unit = {},
    onContinueButtonClicked: () -> Unit = {},
) {
    val userInfoUiState = UserInfoViewModel.uiState.collectAsState()

    GlobalLayout(
        headerTitle = stringResource(R.string.contact_info),
        onCancelButtonClicked = onCancelButtonClicked
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 40.pixelToSecondaryDp),
            horizontalArrangement = Arrangement.spacedBy(32.pixelToSecondaryDp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.pixelToSecondaryDp),
                    value = userInfoUiState.value.firstName,
                    onValueChange = { UserInfoViewModel.updateFirstName(it) },
                    label = { Text(stringResource(R.string.first_name)) },
                    maxLines = 1,
                    singleLine = true,
                    enabled = false,
                    readOnly = true,
                    isError = userInfoUiState.value.errors.containsKey("firstName"),
                )
                if (userInfoUiState.value.errors.containsKey("firstName")) {
                    ErrorMessageText(
                        text = stringResource(userInfoUiState.value.errors["firstName"]!!.string),
                        isSecondaryScreen = true
                    )
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.pixelToSecondaryDp),
                    value = userInfoUiState.value.lastName,
                    onValueChange = { UserInfoViewModel.updateLastName(it) },
                    label = { Text(stringResource(R.string.last_name)) },
                    maxLines = 1,
                    singleLine = true,
                    enabled = false,
                    readOnly = true,
                    isError = userInfoUiState.value.errors.containsKey("lastName"),
                )
                if (userInfoUiState.value.errors.containsKey("lastName")) {
                    ErrorMessageText(
                        text = stringResource(userInfoUiState.value.errors["lastName"]!!.string),
                        isSecondaryScreen = true
                    )
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(32.pixelToSecondaryDp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.pixelToSecondaryDp),
                    value = userInfoUiState.value.phoneNumber,
                    onValueChange = { UserInfoViewModel.updatePhoneNumber(it) },
                    label = { Text(stringResource(R.string.phone_number)) },
                    maxLines = 1,
                    singleLine = true,
                    enabled = false,
                    readOnly = true,
                    isError = userInfoUiState.value.errors.containsKey("phoneNumber"),
                )
                if (userInfoUiState.value.errors.containsKey("phoneNumber")) {
                    ErrorMessageText(
                        text = stringResource(userInfoUiState.value.errors["phoneNumber"]!!.string),
                        isSecondaryScreen = true
                    )
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.pixelToSecondaryDp),
                    value = userInfoUiState.value.email,
                    onValueChange = { UserInfoViewModel.updateEmail(it) },
                    label = { Text(stringResource(R.string.email)) },
                    maxLines = 1,
                    singleLine = true,
                    enabled = false,
                    readOnly = true,
                    isError = userInfoUiState.value.errors.containsKey("email"),
                )
                if (userInfoUiState.value.errors.containsKey("email")) {
                    ErrorMessageText(
                        text = stringResource(userInfoUiState.value.errors["email"]!!.string),
                        isSecondaryScreen = true
                    )
                }
            }
        }
        CustomButton(
            modifier = Modifier
                .width(305.pixelToSecondaryDp)
                .padding(top = 40.pixelToSecondaryDp),
            buttonText = stringResource(R.string.continues),
            disabled = userInfoUiState.value.errors.isNotEmpty(),
            onClick = onContinueButtonClicked
        )
    }
}

@Preview(showBackground = true, widthDp = 1080)
@Composable
fun UserInfoScreenPreview() {
    LobbyAppTheme {
        UserInfoScreen()
    }
}
