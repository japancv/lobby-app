package com.example.lobbyapp.util

import android.annotation.SuppressLint
import android.media.Image
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.lifecycle.Lifecycle
import com.example.lobbyapp.data.ApiClient
import com.example.lobbyapp.model.Base64Image
import com.example.lobbyapp.model.Identity
import com.example.lobbyapp.model.SearchRequest
import com.example.lobbyapp.network.IdpApiService
import com.example.lobbyapp.ui.viewModel.FaceDetectionViewModel
import com.example.lobbyapp.ui.viewModel.UserInfoViewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class FaceAnalyzer(
    lifecycle: Lifecycle,
    faceDetectionViewModel: FaceDetectionViewModel,
    threshold: Double,
    navigateToWaitForConfirmation: () -> Unit = {},
    navigateToCannotRecognize: () -> Unit = {},
    onError: (error: Exception) -> Unit = {}
) : ImageAnalysis.Analyzer {
    private val apiClient =
        ApiClient.getInstance().create(IdpApiService::class.java)
    private val options = FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
        .setMinFaceSize(0.15f)
        .build()
    private val detector = FaceDetection.getClient(options)
    private var evaluating: Boolean
    private var navigated: Boolean
    private var _faceDetectionViewModel: FaceDetectionViewModel
    private var _threshold: Double
    private var _navigateToCannotRecognize: () -> Unit
    private var _navigateToWaitForConfirmation: () -> Unit
    private var _onError: (error: Exception) -> Unit

    init {
        //add the detector in lifecycle observer to properly close it when it's no longer needed.
        lifecycle.addObserver(detector)
        _faceDetectionViewModel = faceDetectionViewModel
        _threshold = threshold
        _navigateToCannotRecognize = navigateToCannotRecognize
        _navigateToWaitForConfirmation = navigateToWaitForConfirmation
        _onError = onError
        evaluating = false
        navigated = false
    }

    override fun analyze(imageProxy: ImageProxy) {
        _faceDetectionViewModel.setPreview(imageProxy.width, imageProxy.height)
        detectFaces(imageProxy)
    }

    @SuppressLint("UnsafeExperimentalUsageError", "UnsafeOptInUsageError")
    private fun detectFaces(imageProxy: ImageProxy) {
        val image = InputImage.fromMediaImage(
            imageProxy.image as Image,
            imageProxy.imageInfo.rotationDegrees
        )

        detector.process(image)
            .addOnSuccessListener { faces ->
                Log.d(TAG, "Number of face detected: " + faces.size)
                val isError = _faceDetectionViewModel.uiState.value.error != null

                if (!isError) {
                    _faceDetectionViewModel.setFaces(faces)
                }

                if (!evaluating && !isError && faces.size > 0) {
                    evaluating = true
                    imageProxy.image?.let { image ->
                        val base64Image = imageToBase64(image)

                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                val response = apiClient.search(
                                    SearchRequest(
                                        image = Base64Image(data = base64Image),
                                        threshold = _threshold,
                                        maxReturns = 1
                                    )
                                )
                                withContext(Dispatchers.Main) {
                                    if (response.result) {
                                        if (!navigated) {
                                            navigated = true
                                            updateUserInfo(
                                                identities = response.identities,
                                                image = base64Image
                                            )
                                            _navigateToWaitForConfirmation()
                                        }
                                    } else {
                                        if (!navigated) {
                                            navigated = true
                                            UserInfoViewModel.updateImage(
                                                image = base64Image
                                            )
                                            _navigateToCannotRecognize()
                                        }
                                    }
                                }
                            } catch (e: IOException) {
                                Log.d("FaceAnalyzer Error", e.toString())
                                _onError(e)
                            } catch (e: HttpException) {
                                Log.d("FaceAnalyzer Error", e.toString())
                                _onError(e)
                            } finally {
                                evaluating = false
                            }
                        }
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Face analysis failure.", e)
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    }

    private fun updateUserInfo(identities: List<Identity>, image: String) {
        if (identities.isNotEmpty()) {
            val identity = identities[0]

            UserInfoViewModel.updateUserInfo(
                isRegister = false,
                id = identity.userId,
                firstName = identity.firstName,
                lastName = identity.lastName,
                phoneNumber = identity.phoneNumber,
                email = identity.email,
                image = image,
                groups = identity.groups
            )
        }
    }

    companion object {
        private const val TAG = "FaceAnalyzer"
    }
}