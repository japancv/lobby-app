package com.example.lobbyapp.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.lobbyapp.LobbyAppApplication
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.util.*

data class StandbyUiState(
    var isCameraOn: Boolean = true,
)

class StandbyViewModel(private val configProperties: Properties) : ViewModel() {
    val uiState = MutableStateFlow(StandbyUiState())
    val isCameraAlwaysOn = configProperties.getProperty("CAMERA_ALWAYS_ON")
        .lowercase(locale = Locale.ENGLISH) == "true"

    init {
        reset()
    }

    fun reset() {
        uiState.value = StandbyUiState(
            isCameraOn = isCameraAlwaysOn
        )
    }

    fun toggleIsCameraOn() {
        uiState.update { currentState ->
            currentState.copy(
                isCameraOn = !currentState.isCameraOn
            )
        }
    }

    /**
     * Factory for [StandbyViewModel] that takes [Properties] as a dependency
     */
    companion object {
        // Create a singleton instance of StandByViewModel
        private var instance: StandbyViewModel? = null

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as LobbyAppApplication)

                if (instance == null) {
                    instance =
                        StandbyViewModel(configProperties = application.container.configProperties)
                }

                instance!!
            }
        }
    }
}