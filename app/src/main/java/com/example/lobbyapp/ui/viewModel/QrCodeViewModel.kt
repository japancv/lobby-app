package com.example.lobbyapp.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

data class QrCodeState(
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

    /**
     * Factory for [QrCodeViewModel]
     */
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                QrCodeViewModel()
            }
        }
    }
}