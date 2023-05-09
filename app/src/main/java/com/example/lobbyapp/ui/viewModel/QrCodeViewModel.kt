package com.example.lobbyapp.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

data class QrCodeState(
    var enabled: Boolean = false,
    var encodedUserId: String = "",
    var decodedUserId: String = "",
    var error: String? = null,
)

class QrCodeViewModel : ViewModel() {
    var uiState = MutableStateFlow(QrCodeState())

    init {
        resetQrCode()
    }

    private fun resetQrCode() {
        uiState.value =
            QrCodeState(
                enabled = false,
                encodedUserId = "",
                decodedUserId = "",
                error = null
            )
    }

    fun setEncodedUserId(encodedUserId: String) {
        uiState.update { currentState ->
            currentState.copy(
                encodedUserId = encodedUserId
            )
        }
    }

    fun setError(error: String?) {
        uiState.update { currentState ->
            currentState.copy(
                encodedUserId = "",
                decodedUserId = "",
                error = error
            )
        }
    }

    fun setEnabled(enabled: Boolean) {
        uiState.update { currentState ->
            currentState.copy(
                enabled = enabled
            )
        }
    }

    /**
     * Factory for [QrCodeViewModel]
     */
    companion object {
        // Create a singleton instance of QrCodeViewModel
        private val instance = QrCodeViewModel()

        // The Factory that returns the singleton instance of QrCodeViewModel
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                instance
            }
        }
    }
}