package com.example.lobbyapp.ui.customer

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lobbyapp.R
import com.example.lobbyapp.ui.component.GlobalLayout
import com.example.lobbyapp.ui.theme.LobbyAppTheme
import com.example.lobbyapp.ui.theme.body5

@Composable
fun AccessGrantedScreen(
    onCancelButtonClicked: () -> Unit = {}
) {
    GlobalLayout(
        onCancelButtonClicked = onCancelButtonClicked
    ) {
        Image(
            painter = painterResource(id = R.drawable.correct),
            contentDescription = "correct",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .padding(bottom = 16.dp),
        )
        Text(
            text = stringResource(R.string.title_access_granted),
            modifier = Modifier.padding(vertical = 16.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h2
        )
        Text(
            text = stringResource(R.string.subtitle_access_granted),
            style = MaterialTheme.typography.body5,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.secondary
        )
    }
}

@Preview
@Composable
fun HeaderPreview() {
    LobbyAppTheme {
        AccessGrantedScreen()
    }
}