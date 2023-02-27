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
import com.example.lobbyapp.util.FaceAnalyzer
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
    navigateToWaitForConfirmation: () -> Unit = {},
    navigateToCannotRecognize: () -> Unit = {},
    onError: (error: Exception) -> Unit = {},
) {
    val context = LocalContext.current
    val application = LocalContext.current.applicationContext as LobbyAppApplication
    val irCameraManager by remember { mutableStateOf(context.getSystemService(Context.CAMERA_SERVICE) as CameraManager) }
    val preview = Preview.Builder().build()
    val lifecycleOwner = LocalLifecycleOwner.current
    val executor = ContextCompat.getMainExecutor(context)
    val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
    val previewView = remember {
        PreviewView(context).apply {
            scaleX = -1F
            implementationMode = PreviewView.ImplementationMode.COMPATIBLE
        }
    }
    val analysisUseCase = ImageAnalysis.Builder()
        .build()
        .also {
            it.setAnalyzer(
                executor,
                FaceAnalyzer(
                    lifecycle = lifecycleOwner.lifecycle,
                    faceDetectionViewModel = faceDetectionViewModel,
                    threshold = application.container.configProperties.getProperty("THRESHOLD")
                        .toDouble(),
                    navigateToWaitForConfirmation = navigateToWaitForConfirmation,
                    navigateToCannotRecognize = navigateToCannotRecognize,
                    onError = onError
                )
            )
        }

    LaunchedEffect(true) {
        val cameraProvider = context.getCameraProvider()

        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            preview,
            analysisUseCase
        )
        irCameraManager.openCamera(
            CameraCharacteristics.LENS_FACING_FRONT.toString(),
            Executors.newSingleThreadExecutor(),
            object : CameraDevice.StateCallback() {
                override fun onOpened(camera: CameraDevice) {}
                override fun onDisconnected(p0: CameraDevice) {}
                override fun onError(p0: CameraDevice, p1: Int) {
                    onError(IOException())
                }
            }
        )
        preview.setSurfaceProvider(previewView.surfaceProvider)
    }

    Box(contentAlignment = Alignment.BottomCenter, modifier = modifier.fillMaxSize()) {
        AndroidView({ previewView }, modifier = Modifier.fillMaxSize())
    }
}