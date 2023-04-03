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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lobbyapp.R
import com.example.lobbyapp.ui.component.*
import com.example.lobbyapp.ui.viewModel.FaceDetectionViewModel
import com.example.lobbyapp.ui.viewModel.QrCodeViewModel
import com.example.lobbyapp.view.FaceOverlay

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun FaceRecognitionScreen(
    navigateToWelcomeScreen: () -> Unit = {},
    navigateToCannotRecognize: () -> Unit = {},
    navigateToWaitForConfirmation: () -> Unit = {}
) {
    val faceDetectionViewModel: FaceDetectionViewModel =
        viewModel(factory = FaceDetectionViewModel.Factory)
    val faceDetectionState = faceDetectionViewModel.uiState.collectAsState()

    val qrCodeViewModel: QrCodeViewModel =
        viewModel(factory = QrCodeViewModel.Factory)
    val qrCodeState = qrCodeViewModel.uiState.collectAsState()

    val (cameraError, setCameraError) = remember { mutableStateOf<Exception?>(null) }

    if (qrCodeState.value.error != null) {
        ErrorDialog(
            errorMessage = qrCodeState.value.error,
            onConfirm = {
                qrCodeViewModel.setError(null)
            })
    } else if (faceDetectionState.value.error != null) {
        ErrorDialog(
            errorMessage = faceDetectionState.value.error,
            onConfirm = {
                faceDetectionViewModel.setError(null)
            })
    } else if (cameraError != null) {
        ErrorDialog(
            errorMessage = stringResource(R.string.camera_error_message),
            onConfirm = {
                setCameraError(null)
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
            qrCodeViewModel = qrCodeViewModel,
            navigateToWelcomeScreen = navigateToWelcomeScreen,
            navigateToCannotRecognize = navigateToCannotRecognize,
            navigateToWaitForConfirmation = navigateToWaitForConfirmation,
            onError = fun(error) {
                setCameraError(error)
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
