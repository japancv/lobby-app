package com.example.lobbyapp.ui.customer

import android.annotation.SuppressLint
import android.os.Build
import android.util.Size
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lobbyapp.R
import com.example.lobbyapp.ui.component.CameraView
import com.example.lobbyapp.ui.component.ErrorDialog
import com.example.lobbyapp.ui.component.Header
import com.example.lobbyapp.ui.component.StateBottomAppBar
import com.example.lobbyapp.ui.viewModel.FaceDetectionViewModel
import com.example.lobbyapp.view.FaceOverlay

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun FaceRecognitionScreen(
    navigateToWaitForConfirmation: () -> Unit = {},
    navigateToCannotRecognize: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val showErrorDialog = remember { mutableStateOf(false) }
    val faceDetectionViewModel: FaceDetectionViewModel =
        viewModel(factory = FaceDetectionViewModel.Factory)
    val faceDetectionState = faceDetectionViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            Header(
                buttonText = "",
                bottomBorder = false
            )
        },
        bottomBar = { StateBottomAppBar(initialState = stringResource(R.string.scanning)) },
        modifier = modifier.fillMaxSize(),
    ) {
        if (showErrorDialog.value) {
            ErrorDialog(onConfirm = {
                showErrorDialog.value = false
            })
        }

        CameraView(
            modifier = modifier,
            faceDetectionViewModel = faceDetectionViewModel,
            navigateToWaitForConfirmation = navigateToWaitForConfirmation,
            navigateToCannotRecognize = navigateToCannotRecognize,
            onError = {
                showErrorDialog.value = true
            }
        )
        AndroidView(
            modifier = Modifier.fillMaxSize(),
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
