package com.example.lobbyapp.ui.customer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lobbyapp.R
import com.example.lobbyapp.ui.component.CustomButton
import com.example.lobbyapp.ui.component.GlobalLayout
import com.example.lobbyapp.ui.theme.LobbyAppTheme
import com.example.lobbyapp.ui.viewModel.UserInfoViewModel
import com.example.lobbyapp.util.pixelToSecondaryDp

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UserInfoScreen(
    modifier: Modifier = Modifier,
    onCancelButtonClicked: () -> Unit = {},
    onContinueButtonClicked: () -> Unit = {},
) {
    val userInfoUiState = UserInfoViewModel.uiState.collectAsState()
    val keyboard = LocalSoftwareKeyboardController.current

    GlobalLayout(onCancelButtonClicked = onCancelButtonClicked) {
        Text(
            text = stringResource(id = R.string.user_info_title),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            style = MaterialTheme.typography.h4,
            textAlign = TextAlign.Center,
        )
        Column(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = userInfoUiState.value.firstName,
                onValueChange = { UserInfoViewModel.updateFirstName(it) },
                label = { Text(stringResource(R.string.first_name)) },
                maxLines = 1,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                isError = userInfoUiState.value.errors.containsKey("firstName"),
            )
            if (userInfoUiState.value.errors.containsKey("firstName")) {
                ErrorMessageText(
                    text = stringResource(userInfoUiState.value.errors["firstName"]!!.string)
                )
            }
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = userInfoUiState.value.lastName,
                onValueChange = { UserInfoViewModel.updateLastName(it) },
                label = { Text(stringResource(R.string.last_name)) },
                maxLines = 1,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                isError = userInfoUiState.value.errors.containsKey("lastName")
            )
            if (userInfoUiState.value.errors.containsKey("lastName")) {
                ErrorMessageText(
                    text = stringResource(userInfoUiState.value.errors["lastName"]!!.string)
                )
            }
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = userInfoUiState.value.phoneNumber,
                onValueChange = { UserInfoViewModel.updatePhoneNumber(it) },
                label = { Text(stringResource(R.string.phone_number)) },
                maxLines = 1,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Next
                )
            )
            if (userInfoUiState.value.errors.containsKey("phoneNumber")) {
                ErrorMessageText(
                    text = stringResource(userInfoUiState.value.errors["phoneNumber"]!!.string)
                )
            }
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = userInfoUiState.value.email,
                onValueChange = { UserInfoViewModel.updateEmail(it) },
                label = { Text(stringResource(R.string.email)) },
                maxLines = 1,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { keyboard?.hide() })
            )
            if (userInfoUiState.value.errors.containsKey("email")) {
                ErrorMessageText(
                    text = stringResource(userInfoUiState.value.errors["email"]!!.string)
                )
            }
            CustomButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                buttonText = stringResource(R.string.continues),
                disabled = userInfoUiState.value.errors.isNotEmpty(),
                onClick = onContinueButtonClicked
            )
        }
    }
}

@Composable
fun ErrorMessageText(text: String, isSecondaryScreen: Boolean = false) {
    Text(
        text = text,
        modifier = Modifier.padding(top = if (isSecondaryScreen) 8.pixelToSecondaryDp else 0.dp),
        color = MaterialTheme.colors.error,
        style = MaterialTheme.typography.caption
    )
}

@Preview(showBackground = true)
@Composable
fun GameScreenPreview() {
    LobbyAppTheme {
        UserInfoScreen()
    }
}
