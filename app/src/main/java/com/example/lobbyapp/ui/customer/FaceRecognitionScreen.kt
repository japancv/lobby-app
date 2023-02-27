package com.example.lobbyapp.ui.customer

import android.annotation.SuppressLint
import android.os.Build
import android.util.Size
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lobbyapp.R
import com.example.lobbyapp.ui.component.*
import com.example.lobbyapp.ui.viewModel.FaceDetectionViewModel
import com.example.lobbyapp.view.FaceOverlay

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun FaceRecognitionScreen(
    navigateToWaitForConfirmation: () -> Unit = {},
    navigateToCannotRecognize: () -> Unit = {}
) {
    val faceDetectionViewModel: FaceDetectionViewModel =
        viewModel(factory = FaceDetectionViewModel.Factory)
    val faceDetectionState = faceDetectionViewModel.uiState.collectAsState()

    if (faceDetectionState.value.error != null) {
        ErrorDialog(onConfirm = {
            faceDetectionViewModel.setError(null)
        })
    }

    Scaffold(
        topBar = {
            Header(
                buttonText = "",
                bottomBorder = false
            )
        },
        bottomBar = { StateBottomAppBar(initialState = stringResource(R.string.scanning)) },
        modifier = Modifier.fillMaxSize(),
    ) {
        CameraView(
            modifier = Modifier,
            faceDetectionViewModel = faceDetectionViewModel,
            navigateToWaitForConfirmation = navigateToWaitForConfirmation,
            navigateToCannotRecognize = navigateToCannotRecognize,
            onError = fun(error) {
                faceDetectionViewModel.setError(error)
            }
        )
        AndroidView(
            factory = { ctx ->
                FaceOverlay(ctx, null)
            },
            update = { view ->
                view.setPreviewSize(
                    Size(
                        faceDetectionState.value.previewWidth,
                        faceDetectionState.value.previewHeight
                    )
                )
                view.setFaces(faceDetectionState.value.faces)
            }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.N)
@Preview(
    heightDp = 640,
    widthDp = 400
)
@Composable
fun FaceRecognitionScreenPreview() {
    FaceRecognitionScreen()
}
