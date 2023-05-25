package com.example.lobbyapp.ui.component

import android.os.Environment
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.lobbyapp.LobbyAppApplication
import com.example.lobbyapp.R
import com.example.lobbyapp.ui.theme.LobbyAppTheme
import com.example.lobbyapp.ui.theme.body3
import com.example.lobbyapp.ui.theme.body5
import com.example.lobbyapp.util.pixelToSecondaryDp
import com.example.lobbyapp.util.toSecondarySp
import java.io.File

@Composable
fun Header(
    isSecondaryDisplay: Boolean = false,
    onCancelButtonClicked: () -> Unit = {},
    bottomBorder: Boolean = true,
    buttonText: String = stringResource(R.string.cancel),
    title: String = "",
) {
    val application = LocalContext.current.applicationContext as LobbyAppApplication

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height((if (isSecondaryDisplay) 120.pixelToSecondaryDp else 70.dp).let { it })
                .padding(top = 12.dp, bottom = 12.dp, start = 20.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    application.container.configProperties.getProperty("LOGO")
                ).absolutePath,
                contentDescription = "logo",
                contentScale = ContentScale.FillHeight
            )
            Text(
                text = title,
                style = (if (isSecondaryDisplay) MaterialTheme.typography.h2.toSecondarySp() else MaterialTheme.typography.h3).let { it }
            )
            TextButton(onClick = onCancelButtonClicked) {
                Text(
                    text = buttonText,
                    color = MaterialTheme.colors.secondary,
                    style = (if (isSecondaryDisplay) MaterialTheme.typography.body3.toSecondarySp() else MaterialTheme.typography.body5).let { it }
                )
            }
        }

        if (bottomBorder) {
            Divider(color = Color.LightGray, thickness = 1.dp)
        }
    }
}


@Preview(showBackground = true, widthDp = 1080)
@Composable
fun HeaderPreview() {
    LobbyAppTheme {
        Header(title = "titlg")
    }
}

