package com.example.lobbyapp.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@Composable
fun DialogContainer(content: @Composable BoxScope.() -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .zIndex(1f)
            .clickable(enabled = false, onClick = {}),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
fun CustomDialog(
    modifier: Modifier = Modifier,
    title: @Composable (() -> Unit)? = null,
    text: @Composable (() -> Unit)? = null,
    confirmButton: @Composable (() -> Unit)? = null,
    dismissButton: @Composable() (() -> Unit?)? = null,
) {
    DialogContainer {
        Card(
            modifier = modifier.fillMaxWidth(0.8f),
            backgroundColor = Color.White,
            shape = RoundedCornerShape(8.dp),
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                if (title != null) {
                    title()
                }
                if (text != null) {
                    text()
                }
                if (confirmButton != null || dismissButton != null) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp),
                        horizontalArrangement = Arrangement.End,
                    ) {
                        if (dismissButton != null) {
                            dismissButton()
                        }
                        if (confirmButton != null) {
                            confirmButton()
                        }
                    }
                }

            }
        }
    }
}
