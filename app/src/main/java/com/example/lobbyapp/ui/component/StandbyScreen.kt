package com.example.lobbyapp.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.zIndex

@Composable
fun StandbyScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black)
            .zIndex(1000F),
    )
}