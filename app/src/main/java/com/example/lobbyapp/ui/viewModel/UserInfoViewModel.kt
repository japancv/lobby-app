package com.example.lobbyapp.ui.viewModel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lobbyapp.R
import com.example.lobbyapp.data.ApiClient
import com.example.lobbyapp.model.CreateIdentityRequest
import com.example.lobbyapp.model.Portrait
import com.example.lobbyapp.network.IdpApiService
import com.example.lobbyapp.util.readConfigFromFile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.util.*

const val MAX_LENGTH = 255

enum class ErrorMessage(val string: Int) {
    REQUIRED(R.string.required),
    TOO_LONG(R.string.too_long),
    INVALID_EMAIL(R.string.invalid_email)
}

data class UserInfoUiState(
    var isRegister: Boolean = true,
    var id: String = "",
    var firstName: String = "",
    var lastName: String = "",
    var phoneNumber: String = "",
    var email: String = "",
    var image: String = "",
    var groups: List<String> = listOf(),
    var errors: Map<String, ErrorMessage> = mapOf(),
    var qrCodeScanned: Boolean = false,
)

/**
 * The singleton view model for storing personal info.
 * View model in singleton pattern can be accessed across activities.
 * If it's not necessary to share data between activities, use `class` is better.
 */
object UserInfoViewModel : ViewModel() {
    val uiState = MutableStateFlow(UserInfoUiState())

    init {
        resetForm()
    }

    fun resetForm() {
        uiState.value =
            UserInfoUiState(
                isRegister = true,
                id = UUID.randomUUID().toString(),
                firstName = uiState.value.firstName,
                lastName = uiState.value.lastName,
                phoneNumber = "",
                email = "",
                image = "",
                groups = listOf(),
                errors = mapOf(),
                qrCodeScanned = false
            )
    }

    fun resetNames() {
        uiState.update { currentState ->
            currentState.copy(
                firstName = "",
                lastName = ""
            )
        }
    }

    fun updateQrCodeScanned(
        qrCodeScanned: Boolean
    ) {
        uiState.update { currentState ->
            currentState.copy(
                qrCodeScanned = qrCodeScanned
            )
        }
    }

    fun updateUserInfo(
        isRegister: Boolean,
        id: String,
        firstName: String,
        lastName: String,
        phoneNumber: String,
        email: String,
        image: String,
        groups: List<String>
    ) {
        uiState.update { currentState ->
            currentState.copy(
                isRegister = isRegister,
                id = id,
                firstName = firstName,
                lastName = lastName,
                phoneNumber = phoneNumber,
                email = email,
                image = image,
                groups = groups,
                errors = mapOf()
            )
        }
    }

    fun updateImage(
        image: String
    ) {
        uiState.update { currentState ->
            currentState.copy(
                image = image
            )
        }
    }

    fun updateFirstName(firstName: String) {
        if (firstName.length > MAX_LENGTH) {
            uiState.update { currentState ->
                currentState.copy(
                    errors = currentState.errors + Pair("firstName", ErrorMessage.TOO_LONG)
                )
            }
        } else {
            uiState.update { currentState ->
                currentState.copy(
                    firstName = firstName,
                    errors = currentState.errors - "firstName"
                )
            }
        }
    }

    fun updateLastName(lastName: String) {
        if (lastName.length > MAX_LENGTH) {
            uiState.update { currentState ->
                currentState.copy(
                    errors = currentState.errors + Pair("lastName", ErrorMessage.TOO_LONG)
                )
            }
        } else {
            uiState.update { currentState ->
                currentState.copy(
                    lastName = lastName,
                    errors = currentState.errors - "lastName"
                )
            }
        }
    }

    fun updatePhoneNumber(phoneNumber: String) {
        if (phoneNumber.length > MAX_LENGTH) {
            uiState.update { currentState ->
                currentState.copy(
                    errors = currentState.errors + Pair("phoneNumber", ErrorMessage.TOO_LONG)
                )
            }
        } else {
            uiState.update { currentState ->
                currentState.copy(
                    phoneNumber = phoneNumber,
                    errors = currentState.errors - "phoneNumber"
                )
            }
        }
    }

    fun updateEmail(email: String) {
        if (email.length > MAX_LENGTH) {
            uiState.update { currentState ->
                currentState.copy(
                    errors = currentState.errors + Pair("email", ErrorMessage.TOO_LONG)
                )
            }
        } else if (email.isNotBlank() && !isValidEmail(email)) {
            uiState.update { currentState ->
                currentState.copy(
                    email = email,
                    errors = currentState.errors + Pair("email", ErrorMessage.INVALID_EMAIL)
                )
            }
        } else {
            uiState.update { currentState ->
                currentState.copy(
                    email = email,
                    errors = currentState.errors - "email"
                )
            }
        }
    }

    /**
     * Call API to create an identity
     */
    suspend fun createIdentity(onSuccess: () -> Unit, onError: (e: Exception) -> Unit) {
        viewModelScope.launch {
            val apiClient = ApiClient.getInstance().create(IdpApiService::class.java)
            val properties = readConfigFromFile()

            try {
                apiClient.createIdentity(
                    CreateIdentityRequest(
                        userId = uiState.value.id,
                        firstName = uiState.value.firstName,
                        lastName = uiState.value.lastName,
                        phoneNumber = uiState.value.phoneNumber,
                        phoneCode = "+81",
                        email = uiState.value.email,
                        portrait = Portrait(
                            data = uiState.value.image,
                            pinCode = properties.getProperty("PIN"),
                            qualityCheck = properties.getProperty("QUALITY_CHECK")
                                .lowercase(locale = Locale.ENGLISH) == "true",
                            storeFile = true,
                            storeFace = true
                        ),
                        cardNo = ""
                    )
                )
                onSuccess()
            } catch (e: IOException) {
                onError(e)
            } catch (e: HttpException) {
                onError(e)
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}