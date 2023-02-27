package com.example.lobbyapp.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lobbyapp.ui.theme.LobbyAppTheme


@Composable
fun LoadingDialog() {
    DialogContainer {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(100.dp)
                .background(MaterialTheme.colors.background, shape = RoundedCornerShape(12.dp))
        ) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingDialogPreview() {
    LobbyAppTheme {
        GlobalLayout {
            LoadingDialog()
        }
    }
}