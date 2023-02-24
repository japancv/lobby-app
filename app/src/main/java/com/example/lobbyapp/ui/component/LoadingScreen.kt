package com.example.lobbyapp.ui.component

import androidx.compose.runtime.Composable

@Composable
fun LoadingScreen(isSecondaryDisplay: Boolean = false) {
    GlobalLayout(isSecondaryDisplay = isSecondaryDisplay) {
        LoadingDialog()
    }
}