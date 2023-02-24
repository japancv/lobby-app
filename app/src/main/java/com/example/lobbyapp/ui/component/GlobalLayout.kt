package com.example.lobbyapp.ui.component

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.lobbyapp.R
import com.example.lobbyapp.util.pixelToSecondaryDp

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun GlobalLayout(
    isSecondaryDisplay: Boolean = false,
    isTableView: Boolean = false,
    showHeader: Boolean = true,
    headerTitle: String = "",
    headerBottomBorder: Boolean = true,
    headerButtonText: String = stringResource(R.string.cancel),
    onCancelButtonClicked: () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit,
) {
    Scaffold(
        topBar = {
            if (showHeader) {
                Header(
                    title = headerTitle,
                    bottomBorder = headerBottomBorder,
                    buttonText = headerButtonText,
                    isSecondaryDisplay = isSecondaryDisplay,
                    onCancelButtonClicked = onCancelButtonClicked
                )
            }
        },
    ) {
        var padding = 30.dp
        var verticalAlignment: Arrangement.Vertical = Arrangement.Center
        if (isSecondaryDisplay) {
            padding = 60.pixelToSecondaryDp
        }
        if (isTableView) {
            padding = 0.dp
            verticalAlignment = Arrangement.Top
        }
        Column(
            verticalArrangement = verticalAlignment,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            content = content
        )
    }
}