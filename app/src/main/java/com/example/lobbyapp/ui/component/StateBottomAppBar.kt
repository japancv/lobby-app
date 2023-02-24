package com.example.lobbyapp.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material.BottomAppBar
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lobbyapp.R

@Composable
fun StateBottomAppBar(
    modifier: Modifier = Modifier,
    initialState: String = "",
) {
    Surface(modifier = modifier.requiredHeight(60.dp)) {
        BottomAppBar(
            modifier = modifier.fillMaxSize(),
            backgroundColor = White,
        ) {
            Column {
                Text(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    text = initialState,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.subtitle1,
                )
            }
        }
    }
}

@Preview
@Composable
private fun LogoAppBarWithSingleActionPreview() {
    MaterialTheme {
        Surface {
            StateBottomAppBar(initialState = stringResource(id = R.string.face_recognition))
        }
    }
}