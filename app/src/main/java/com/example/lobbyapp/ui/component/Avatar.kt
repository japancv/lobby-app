package com.example.lobbyapp.ui.component

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.lobbyapp.R
import com.example.lobbyapp.ui.viewModel.ManageIdentityViewModel
import com.example.lobbyapp.util.pixelToSecondaryDp

@Composable
fun Avatar(
    manageIdentityViewModel: ManageIdentityViewModel,
    identityId: String,
) {
    val (bitmap, setBitmap) = remember { mutableStateOf<Bitmap?>(null) }
    LaunchedEffect(identityId) {
        manageIdentityViewModel.getPortrait(identityId, setBitmap)
    }

    if (bitmap == null) {
        Image(
            painter = painterResource(id = R.drawable.blank_avatar),
            contentDescription = "Portrait Placeholder",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(8.pixelToSecondaryDp)),
        )
    } else {
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "Portrait",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(8.pixelToSecondaryDp)),
        )
    }
}
