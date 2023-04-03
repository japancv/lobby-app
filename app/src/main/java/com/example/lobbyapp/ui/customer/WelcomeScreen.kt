package com.example.lobbyapp.ui.customer

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.lobbyapp.R
import com.example.lobbyapp.ui.component.GlobalLayout
import com.example.lobbyapp.ui.theme.body5
import com.example.lobbyapp.ui.viewModel.UserInfoViewModel

@Composable
fun WelcomeScreen(
    onCancelButtonClicked: () -> Unit = {},
) {
    val userInfoUiState = UserInfoViewModel.uiState.collectAsState()

    GlobalLayout(onCancelButtonClicked = onCancelButtonClicked) {
        Image(
            painter = painterResource(id = R.drawable.correct),
            contentDescription = "correct",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .padding(bottom = 16.dp),
        )
        Text(
            text = "${stringResource(R.string.welcome)} ${userInfoUiState.value.firstName} ${userInfoUiState.value.lastName}",
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 16.dp),
            style = MaterialTheme.typography.h1
        )
        Text(
            text = stringResource(R.string.register_photo_hint),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.body5,
            color = MaterialTheme.colors.secondary
        )
    }
}
