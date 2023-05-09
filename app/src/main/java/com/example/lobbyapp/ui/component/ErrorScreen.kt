package com.example.lobbyapp.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.lobbyapp.R
import com.example.lobbyapp.ui.theme.body5
import com.example.lobbyapp.util.toSecondarySp

enum class ErrorType {
    CONFIG_NOT_FOUND,
    CONFIG_REQUIRED_FIELD_MISSING,
    INTERNET_NOT_AVAILABLE,
    ILLEGAL_LOGO_PATH
}

@Composable
fun ErrorScreen(
    isSecondaryDisplay: Boolean = false,
    errorType: ErrorType,
    extraMessage: String = "",
    showCloseButton: Boolean = false,
    onCloseButtonClicked: () -> Unit = {},
) {
    val errorMessage = when (errorType) {
        ErrorType.CONFIG_NOT_FOUND -> stringResource(R.string.error_config_not_found)
        ErrorType.INTERNET_NOT_AVAILABLE -> stringResource(R.string.error_internet_not_available)
        ErrorType.CONFIG_REQUIRED_FIELD_MISSING -> stringResource(R.string.error_config_required_field_missing)
        ErrorType.ILLEGAL_LOGO_PATH -> stringResource(R.string.illegal_logo_path)
    }

    GlobalLayout(isSecondaryDisplay = isSecondaryDisplay, showHeader = false) {
        Text(
            text = errorMessage,
            style = if (isSecondaryDisplay) MaterialTheme.typography.h2.toSecondarySp() else MaterialTheme.typography.h2,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.primary,
        )
        if (extraMessage.isNotBlank()) {
            Text(
                text = extraMessage,
                style = if (isSecondaryDisplay) MaterialTheme.typography.h5.toSecondarySp() else MaterialTheme.typography.h5,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.primary,
                modifier = Modifier.padding(top = 20.dp),
            )
        }
        if (showCloseButton) {
            TextButton(onClick = onCloseButtonClicked) {
                Text(
                    text = stringResource(R.string.close_app),
                    style = MaterialTheme.typography.body5,
                    color = MaterialTheme.colors.primary
                )
            }
        }
    }
}