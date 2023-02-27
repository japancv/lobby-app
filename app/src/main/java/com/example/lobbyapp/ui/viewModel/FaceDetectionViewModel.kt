package com.example.lobbyapp.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.google.mlkit.vision.face.Face
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

data class FaceDetectionState(
    var faces: List<Face> = emptyList<Face>(),
    var previewWidth: Int = 1,
    var previewHeight: Int = 1,
    var error: Exception? = null,
)

class FaceDetectionViewModel : ViewModel() {
    var uiState = MutableStateFlow(FaceDetectionState())

    init {
        resetFaces()
    }

    fun resetFaces() {
        uiState.value =
            FaceDetectionState(
                faces = emptyList<Face>(),
                previewWidth = 1,
                previewHeight = 1,
                error = null
            )
    }

    fun setFaces(faces: List<Face>) {
        uiState.update { currentState ->
            currentState.copy(
                faces = faces
            )
        }
    }

    fun setPreview(width: Int, height: Int) {
        uiState.update { currentState ->
            currentState.copy(
                previewWidth = width,
                previewHeight = height
            )
        }
    }

    fun setError(error: Exception?) {
        println("setError: $error")
        uiState.update { currentState ->
            currentState.copy(
                faces = emptyList(),
                error = error
            )
        }
    }

    /**
     * Factory for [FaceDetectionViewModel]
     */
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                FaceDetectionViewModel()
            }
        }
    }
}