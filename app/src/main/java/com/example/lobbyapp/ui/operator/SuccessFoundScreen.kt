package com.example.lobbyapp.ui.operator

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lobbyapp.R
import com.example.lobbyapp.ui.component.CustomButton
import com.example.lobbyapp.ui.component.GlobalLayout
import com.example.lobbyapp.ui.theme.LobbyAppTheme
import com.example.lobbyapp.ui.theme.body4
import com.example.lobbyapp.ui.viewModel.UserInfoViewModel
import com.example.lobbyapp.util.pixelToSecondaryDp
import com.example.lobbyapp.util.toSecondarySp

@Composable
fun SuccessFoundScreen(
    onCancelButtonClicked: () -> Unit = {},
    onCheckInButtonClicked: () -> Unit = {},
) {
    val userInfoUiState = UserInfoViewModel.uiState.collectAsState()

    GlobalLayout(headerBottomBorder = false, onCancelButtonClicked = onCancelButtonClicked) {
        Text(
            modifier = Modifier.padding(bottom = 36.dp),
            text = stringResource(R.string.check_in_question),
            style = MaterialTheme.typography.h3.toSecondarySp()
        )
        InfoRow(
            key = stringResource(R.string.name),
            value = "${userInfoUiState.value.firstName} ${userInfoUiState.value.lastName}",
        )
        InfoRow(
            key = stringResource(R.string.phone_number),
            value = userInfoUiState.value.phoneNumber,
        )
        InfoRow(
            key = stringResource(R.string.email),
            value = userInfoUiState.value.email,
        )
        Text(
            stringResource(R.string.check_in_question_hint),
            modifier = Modifier.padding(vertical = 12.dp),
            style = MaterialTheme.typography.body4.toSecondarySp(),
            textAlign = TextAlign.Center
        )
        CustomButton(
            buttonText = stringResource(R.string.check_in),
            modifier = Modifier.padding(top = 16.dp),
            onClick = {
                onCheckInButtonClicked()
            },
        )
    }
}

@Composable
fun InfoRow(key: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(0.6f),
        horizontalArrangement = Arrangement.spacedBy(40.pixelToSecondaryDp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            key,
            style = MaterialTheme.typography.h4.toSecondarySp(),
            modifier = Modifier.fillMaxWidth(0.25f)
        )
        Text(
            value,
            style = MaterialTheme.typography.body4.toSecondarySp(),
            modifier = Modifier.weight(1f)
        )
    }
    Spacer(modifier = Modifier.height(24.dp))
}

@Preview(showBackground = true, widthDp = 1080)
@Composable
fun SuccessFoundScreenPreview() {
    LobbyAppTheme {
        SuccessFoundScreen()
    }
}