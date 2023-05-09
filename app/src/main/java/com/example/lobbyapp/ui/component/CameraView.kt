package com.example.lobbyapp.ui.component

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.lobbyapp.LobbyAppApplication
import com.example.lobbyapp.ui.viewModel.FaceDetectionViewModel
import com.example.lobbyapp.ui.viewModel.QrCodeViewModel
import com.example.lobbyapp.ui.viewModel.StandbyViewModel
import com.example.lobbyapp.util.ImageAnalyzer
import java.io.IOException
import java.util.*
import java.util.concurrent.Executors
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


private suspend fun Context.getCameraProvider(): ProcessCameraProvider =
    suspendCoroutine { continuation ->
        ProcessCameraProvider.getInstance(this).also { cameraProvider ->
            cameraProvider.addListener({
                continuation.resume(cameraProvider.get())
            }, ContextCompat.getMainExecutor(this))
        }
    }

@SuppressLint("MissingPermission", "NewApi")
@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun CameraView(
    modifier: Modifier,
    faceDetectionViewModel: FaceDetectionViewModel,
    qrCodeViewModel: QrCodeViewModel,
    standbyViewModel: StandbyViewModel,
    navigateToWelcomeScreen: () -> Unit = {},
    navigateToCannotRecognize: () -> Unit = {},
    navigateToWaitForCheckInConfirmation: () -> Unit = {},
    navigateToWaitForConfirmation: () -> Unit = {},
    onError: (error: Exception) -> Unit = {},
) {
    val context = LocalContext.current
    val application = LocalContext.current.applicationContext as LobbyAppApplication
    val (irCameraManager) = remember { mutableStateOf(context.getSystemService(Context.CAMERA_SERVICE) as CameraManager) }
    val standbyUiState = standbyViewModel.uiState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val executor = ContextCompat.getMainExecutor(context)
    val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
    val preview = Preview.Builder().build()
    val previewView = remember {
        PreviewView(context).apply {
            scaleX = -1F
            implementationMode = PreviewView.ImplementationMode.COMPATIBLE
        }
    }

    val imageAnalyzer = ImageAnalyzer(
        lifecycle = lifecycleOwner.lifecycle,
        faceDetectionViewModel = faceDetectionViewModel,
        qrCodeViewModel = qrCodeViewModel,
        threshold = application.container.configProperties.getProperty("THRESHOLD")
            .toDouble(),
        navigateToWelcomeScreen = navigateToWelcomeScreen,
        navigateToCannotRecognize = navigateToCannotRecognize,
        navigateToWaitForCheckInConfirmation = navigateToWaitForCheckInConfirmation,
        navigateToWaitForConfirmation = navigateToWaitForConfirmation,
    )

    val analysisUseCase = ImageAnalysis.Builder()
        .build()
        .also {
            it.setAnalyzer(executor, imageAnalyzer)
        }

    LaunchedEffect(standbyUiState.value) {
        val cameraProvider = context.getCameraProvider()

        if (standbyUiState.value.isCameraOn) {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                analysisUseCase
            )
            preview.setSurfaceProvider(previewView.surfaceProvider)
            irCameraManager.openCamera(
                CameraCharacteristics.LENS_FACING_FRONT.toString(),
                Executors.newSingleThreadScheduledExecutor(),
                object : CameraDevice.StateCallback() {
                    override fun onOpened(camera: CameraDevice) {}
                    override fun onDisconnected(p0: CameraDevice) {}
                    override fun onError(p0: CameraDevice, p1: Int) {
                        onError(IOException())
                    }
                }
            )
        } else {
            cameraProvider.unbindAll()
        }
    }

    if (standbyUiState.value.isCameraOn) {
        Box(contentAlignment = Alignment.BottomCenter, modifier = modifier.fillMaxSize()) {
            AndroidView({ previewView }, modifier = Modifier.fillMaxSize())
        }
    }
}
