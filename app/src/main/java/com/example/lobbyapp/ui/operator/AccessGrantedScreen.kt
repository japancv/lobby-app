package com.example.lobbyapp.ui.operator

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lobbyapp.R
import com.example.lobbyapp.ui.component.CustomButton
import com.example.lobbyapp.ui.component.GlobalLayout
import com.example.lobbyapp.ui.theme.LobbyAppTheme
import com.example.lobbyapp.util.toSecondarySp

@Composable
fun AccessGrantedScreen(
    onGoToHomeButtonClicked: () -> Unit = {},
) {
    GlobalLayout(
        headerBottomBorder = false,
        headerButtonText = "End"
    ) {
        Text(
            text = stringResource(R.string.title_access_granted),
            style = MaterialTheme.typography.h1.toSecondarySp()
        )
        CustomButton(
            modifier = Modifier.padding(top = 80.dp),
            buttonText = stringResource(R.string.home),
            background = MaterialTheme.colors.background,
            textColor = MaterialTheme.colors.primary,
            onClick = onGoToHomeButtonClicked,
            border = BorderStroke(1.dp, MaterialTheme.colors.secondary)
        )
    }
}

@Preview(showBackground = true, widthDp = 1080)
@Composable
fun AccessGrantedScreenPreview() {
    LobbyAppTheme { AccessGrantedScreen() }
}
