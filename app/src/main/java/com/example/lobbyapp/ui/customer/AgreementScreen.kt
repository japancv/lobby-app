package com.example.lobbyapp.ui.customer

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lobbyapp.LobbyAppApplication
import com.example.lobbyapp.R
import com.example.lobbyapp.ui.component.CustomButton
import com.example.lobbyapp.ui.component.GlobalLayout
import com.example.lobbyapp.ui.theme.LobbyAppTheme
import com.example.lobbyapp.ui.theme.body5
import com.example.lobbyapp.ui.theme.body6
import com.example.lobbyapp.ui.viewModel.UserInfoViewModel

@Composable
fun AgreementScreen(
    onCancelButtonClicked: () -> Unit = {},
    onAgreeButtonClicked: () -> Unit = {},
    onCheckInButtonClicked: () -> Unit = {},
) {
    val application = LocalContext.current.applicationContext as LobbyAppApplication
    val userInfoUiState = UserInfoViewModel.uiState.collectAsState()

    GlobalLayout(
        onCancelButtonClicked = onCancelButtonClicked
    ) {
        Text(
            text = stringResource(R.string.title_read_and_confirm),
            modifier = Modifier.padding(bottom = 16.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h4
        )
        Text(
            text = application.container.configProperties.getProperty("UA"),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .border(width = 1.dp, color = Color.LightGray, shape = RoundedCornerShape(6.dp))
                .padding(10.dp)
                .verticalScroll(rememberScrollState()),
            lineHeight = 24.sp,
            style = MaterialTheme.typography.body6
        )
        CustomButton(
            buttonText = stringResource(R.string.agree),
            modifier = Modifier
                .padding(vertical = 20.dp)
                .fillMaxWidth(),
            onClick = onAgreeButtonClicked
        )
        if (!userInfoUiState.value.qrCodeScanned) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    stringResource(R.string.already_registered_question),
                    style = MaterialTheme.typography.body5
                )
                TextButton(onClick = onCheckInButtonClicked) {
                    Text(
                        stringResource(R.string.check_in),
                        color = MaterialTheme.colors.primary,
                        style = MaterialTheme.typography.h5
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun AgreementScreensPreview() {
    LobbyAppTheme {
        AgreementScreen()
    }
}