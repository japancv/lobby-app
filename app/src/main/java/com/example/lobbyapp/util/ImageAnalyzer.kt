package com.example.lobbyapp.util

import android.annotation.SuppressLint
import android.media.Image
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.lifecycle.Lifecycle
import com.example.lobbyapp.data.ApiClient
import com.example.lobbyapp.model.*
import com.example.lobbyapp.network.IdpApiService
import com.example.lobbyapp.ui.viewModel.FaceDetectionViewModel
import com.example.lobbyapp.ui.viewModel.QrCodeViewModel
import com.example.lobbyapp.ui.viewModel.UserInfoViewModel
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.io.IOException
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class ImageAnalyzer(
    lifecycle: Lifecycle,
    faceDetectionViewModel: FaceDetectionViewModel,
    qrCodeViewModel: QrCodeViewModel,
    threshold: Double,
    navigateToWelcomeScreen: () -> Unit = {},
    navigateToCannotRecognize: () -> Unit = {},
    navigateToWaitForCheckInConfirmation: () -> Unit = {},
    navigateToWaitForConfirmation: () -> Unit = {},
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
    private var _qrCodeViewModel: QrCodeViewModel
    private var _threshold: Double
    private var _navigateToCannotRecognize: () -> Unit
    private var _navigateToWelcomeScreen: () -> Unit
    private var _navigateToWaitForCheckInConfirmation: () -> Unit
    private var _navigateToWaitForConfirmation: () -> Unit

    init {
        //add the detector in lifecycle observer to properly close it when it's no longer needed.
        lifecycle.addObserver(detector)
        _faceDetectionViewModel = faceDetectionViewModel
        _qrCodeViewModel = qrCodeViewModel
        _threshold = threshold
        _navigateToCannotRecognize = navigateToCannotRecognize
        _navigateToWelcomeScreen = navigateToWelcomeScreen
        _navigateToWaitForCheckInConfirmation = navigateToWaitForCheckInConfirmation
        _navigateToWaitForConfirmation = navigateToWaitForConfirmation
        evaluating = false
        navigated = false
    }

    override fun analyze(imageProxy: ImageProxy) {
        _faceDetectionViewModel.setPreview(imageProxy.width, imageProxy.height)
        detectImage(imageProxy)
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun detectImage(imageProxy: ImageProxy) {
        val image = InputImage.fromMediaImage(
            imageProxy.image as Image,
            imageProxy.imageInfo.rotationDegrees
        )

        CoroutineScope(Dispatchers.Default).launch {
            if (_qrCodeViewModel.uiState.value.enabled) {
                barcodeDeferred(image)
            } else {
                faceDeferred(imageProxy, image)
            }

            // Close the imageProxy after both tasks have completed
            imageProxy.close()
        }
    }

    @SuppressLint("UnsafeOptInUsageError")
    private suspend fun faceDeferred(imageProxy: ImageProxy, image: InputImage) {
        suspendCoroutine<String> { continuation ->
            detector.process(image)
                .addOnSuccessListener { faces ->
                    Log.d(TAG, "Number of face detected: " + faces.size)
                    val isFaceError = _faceDetectionViewModel.uiState.value.error != null
                    val isQrError = _qrCodeViewModel.uiState.value.error != null
                    val hasError = isFaceError || isQrError

                    if (!hasError && !navigated) {
                        _faceDetectionViewModel.setFaces(faces)
                    }

                    if (!evaluating && !hasError && faces.size > 0 && !navigated) {
                        evaluating = true
                        imageProxy.image?.let { image ->
                            val base64Image = imageToBase64(image)
                            Log.d(
                                "scanned",
                                UserInfoViewModel.uiState.value.qrCodeScanned.toString()
                            )
                            if (UserInfoViewModel.uiState.value.qrCodeScanned) {
                                updateIdentityPortrait(base64Image)
                            } else {
                                searchUser(base64Image)
                            }
                        }
                    }
                    continuation.resume("")
                }
                .addOnFailureListener { e ->
                    continuation.resumeWithException(e)
                    Log.e(TAG, "Face analysis failure.", e)
                }
        }
    }

    private suspend fun barcodeDeferred(image: InputImage) {
        suspendCoroutine<String> { continuation ->
            val options = BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
                .build()
            val barcodeScanner = BarcodeScanning.getClient(options)

            barcodeScanner.process(image)
                .addOnSuccessListener { barcodes ->
                    val isFaceError = _faceDetectionViewModel.uiState.value.error != null
                    val isQrError = _qrCodeViewModel.uiState.value.error != null
                    val hasError = isFaceError || isQrError

                    if (barcodes.isNotEmpty() && !evaluating && !hasError && !navigated) {
                        evaluating = true
                        barcodes[0].rawValue?.let { barcodeValue ->
                            Log.d("barcode", barcodeValue)
                            _qrCodeViewModel.setEncodedUserId(barcodeValue)
                            UserInfoViewModel.updateQrCodeScanned(true)
                            getIdentity(barcodeValue)
                        }
                    } else {
                        Log.d(TAG, "analyze: No barcode Scanned")
                    }
                    continuation.resume("")
                }
                .addOnFailureListener { e ->
                    continuation.resumeWithException(e)
                    Log.d(TAG, "Barcode Analyser: Something went wrong $e")
                }
        }
    }

    private fun updateIdentityPortrait(base64Image: String) {
        val properties = readConfigFromFile()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                apiClient.updateIdentityPortrait(
                    identityId = UserInfoViewModel.uiState.value.id,
                    UpdateIdentityPortraitRequest(
                        portrait = Portrait(
                            data = base64Image,
                            pinCode = properties.getProperty("PIN"),
                            qualityCheck = properties.getProperty("QUALITY_CHECK")
                                .lowercase(locale = Locale.ENGLISH) == "true",
                            storeFile = true,
                            storeFace = true
                        )
                    )
                )
                withContext(Dispatchers.Main) {
                    if (!navigated) {
                        navigated = true
                        evaluating = false
                        _navigateToWaitForCheckInConfirmation()
                    }
                }
            } catch (e: IOException) {
                if (!navigated) {
                    _faceDetectionViewModel.setError(e.localizedMessage)
                }
            } catch (e: HttpException) {
                val errorResponse = e.response()?.errorBody()

                if (!navigated && errorResponse != null) {
                    Log.d("FaceAnalyzer Error", errorResponse.toString())
                    _faceDetectionViewModel.setError(parseServerErrorResponse(errorResponse)?.message)
                }
            } finally {
                evaluating = false
            }
        }
    }

    private fun getIdentity(barcodeValue: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiClient.getDecodedUserId(
                    DecodedUserIdRequest(
                        data = barcodeValue
                    )
                )
                delay(800L)
                val identityResponse =
                    apiClient.getIdentity(identityId = response.userId)
                withContext(Dispatchers.Main) {
                    if (!navigated) {
                        navigated = true
                        updateUserInfo(
                            identity = identityResponse,
                            image = ""
                        )
                        _navigateToWelcomeScreen()
                    }
                }
            } catch (e: IOException) {
                Log.d("Barcode Error", e.toString())
                UserInfoViewModel.updateQrCodeScanned(false)
                if (!navigated) {
                    _qrCodeViewModel.setError(e.localizedMessage)
                }
            } catch (e: HttpException) {
                val errorResponse = e.response()?.errorBody()

                if (!navigated && errorResponse != null) {
                    Log.d("Barcode Error", errorResponse.toString())
                    UserInfoViewModel.updateQrCodeScanned(false)
                    _qrCodeViewModel.setError(parseServerErrorResponse(errorResponse)?.message)
                }
            } finally {
                evaluating = false
            }
        }
    }

    private fun searchUser(base64Image: String) {
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
                    if (!navigated) {
                        if (response.result) {
                            navigated = true
                            if (response.identities.isNotEmpty()) {
                                updateUserInfo(
                                    identity = response.identities[0],
                                    image = base64Image
                                )

                                _navigateToWaitForCheckInConfirmation()
                            }
                        } else {
                            val isRetakeImage =
                                !UserInfoViewModel.uiState.value.image.isNullOrBlank() && UserInfoViewModel.uiState.value.isRegister
                            navigated = true
                            UserInfoViewModel.updateImage(
                                image = base64Image
                            )

                            if (isRetakeImage) {
                                _navigateToWaitForConfirmation()
                            } else {
                                _navigateToCannotRecognize()
                            }
                        }
                    }
                }
            } catch (e: IOException) {
                Log.d("FaceAnalyzer Error", e.toString())
                if (!navigated) {
                    _faceDetectionViewModel.setError(e.localizedMessage)
                }
            } catch (e: HttpException) {
                val errorResponse = e.response()?.errorBody()

                if (!navigated && errorResponse != null) {
                    Log.d("FaceAnalyzer Error", errorResponse.toString())
                    _faceDetectionViewModel.setError(parseServerErrorResponse(errorResponse)?.message)
                }
            } finally {
                _faceDetectionViewModel.setFaces(emptyList())
                evaluating = false
            }
        }
    }

    private fun updateUserInfo(identity: Identity, image: String) {
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

    companion object {
        private const val TAG = "FaceAnalyzer"
    }
}