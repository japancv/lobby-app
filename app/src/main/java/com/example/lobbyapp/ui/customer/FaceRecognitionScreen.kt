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
import com.example.lobbyapp.ui.viewModel.StandbyViewModel
import com.example.lobbyapp.view.FaceOverlay

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun FaceRecognitionScreen(
    onCancelButtonClicked: () -> Unit = {},
    navigateToWelcomeScreen: () -> Unit = {},
    navigateToCannotRecognize: () -> Unit = {},
    navigateToWaitForCheckInConfirmation: () -> Unit = {},
    navigateToWaitForConfirmation: () -> Unit = {}
) {
    val faceDetectionViewModel: FaceDetectionViewModel =
        viewModel(factory = FaceDetectionViewModel.Factory)
    val faceDetectionState = faceDetectionViewModel.uiState.collectAsState()

    val qrCodeViewModel: QrCodeViewModel =
        viewModel(factory = QrCodeViewModel.Factory)
    val qrCodeState = qrCodeViewModel.uiState.collectAsState()

    val standbyViewModel: StandbyViewModel =
        viewModel(factory = StandbyViewModel.Factory)
    val standbyState = standbyViewModel.uiState.collectAsState()

    val (cameraError, setCameraError) = remember { mutableStateOf<Exception?>(null) }

    if (!standbyState.value.isCameraOn) {
        StandbyScreen()
    } else if (qrCodeState.value.error != null) {
        ErrorDialog(
            errorMessage = qrCodeState.value.error,
            onRetryButtonClicked = {
                qrCodeViewModel.setError(null)
            })
    } else if (faceDetectionState.value.error != null) {
        ErrorDialog(
            errorMessage = faceDetectionState.value.error,
            onRetryButtonClicked = {
                faceDetectionViewModel.setError(null)
            })
    } else if (cameraError != null) {
        ErrorDialog(
            errorMessage = stringResource(R.string.camera_error_message),
            onRetryButtonClicked = {
                setCameraError(null)
            })
    }

    Scaffold(
        topBar = {
            Header(
                buttonText = if (qrCodeState.value.enabled) stringResource(R.string.cancel) else "",
                onCancelButtonClicked = onCancelButtonClicked,
                bottomBorder = false
            )
        },
        bottomBar = {
            StateBottomAppBar(
                initialState = if (qrCodeState.value.enabled)
                    stringResource(R.string.scanning_qr_code) else stringResource(R.string.scanning)
            )
        },
        modifier = Modifier.fillMaxSize(),
    ) {
        CameraView(
            modifier = Modifier,
            faceDetectionViewModel = faceDetectionViewModel,
            qrCodeViewModel = qrCodeViewModel,
            standbyViewModel = standbyViewModel,
            navigateToWelcomeScreen = navigateToWelcomeScreen,
            navigateToCannotRecognize = navigateToCannotRecognize,
            navigateToWaitForCheckInConfirmation = navigateToWaitForCheckInConfirmation,
            navigateToWaitForConfirmation = navigateToWaitForConfirmation,
            onError = {
                setCameraError(it)
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
