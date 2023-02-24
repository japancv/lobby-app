package com.example.lobbyapp.ui.viewModel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.lobbyapp.LobbyAppApplication
import com.example.lobbyapp.data.IdpRepository
import com.example.lobbyapp.model.Identity
import com.example.lobbyapp.model.IdentitySummary
import com.example.lobbyapp.model.IdpGroupSummary
import com.example.lobbyapp.model.RemoveGroupIdentitiesRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

enum class ManageIdentityUiState {
    GetGroupSuccess,
    GetGroupLoading,
    GetGroupsSuccess,
    GetGroupsLoading,
    GetIdentitySuccess,
    GetIdentityLoading,
    RemoveIdentityFromGroupSuccess,
    RemoveIdentityFromGroupLoading,
    Error,
}

class ManageIdentityViewModel(private val idpRepository: IdpRepository) : ViewModel() {
    var uiState: ManageIdentityUiState by mutableStateOf(ManageIdentityUiState.GetGroupsLoading)
    var identitySummaries by mutableStateOf(listOf<IdentitySummary>())
    private val identitiesMap = mutableMapOf<String, Identity>()
    private val portraitMap = mutableMapOf<String, Bitmap>()
    var selectedIds by mutableStateOf(listOf<String>())
    var groups by mutableStateOf(listOf<IdpGroupSummary>())

    fun updateSelectedIds(id: String) {
        selectedIds = if (selectedIds.contains(id)) {
            selectedIds - id
        } else {
            selectedIds + id
        }
    }

    fun selectAll() {
        selectedIds = if (selectedIds.size == identitySummaries.size) {
            emptyList()
        } else {
            identitySummaries.map { identitySummary -> identitySummary.userId }
        }
    }

    fun getGroups() {
        viewModelScope.launch {
            uiState = ManageIdentityUiState.GetGroupsLoading
            uiState = try {
                groups =
                    withContext(Dispatchers.Default) { idpRepository.getGroups() }.groups
                ManageIdentityUiState.GetGroupsSuccess
            } catch (e: IOException) {
                ManageIdentityUiState.Error
            } catch (e: HttpException) {
                ManageIdentityUiState.Error
            }
        }
    }

    fun getGroup(groupId: String, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            uiState = ManageIdentityUiState.GetGroupLoading
            uiState = try {
                identitySummaries =
                    withContext(Dispatchers.Default) { idpRepository.getGroup(groupId) }.identities
                onSuccess()
                ManageIdentityUiState.GetGroupSuccess
            } catch (e: IOException) {
                ManageIdentityUiState.Error
            } catch (e: HttpException) {
                ManageIdentityUiState.Error
            }
        }
    }

    fun getIdentity(identityId: String, onSuccess: (identity: Identity) -> Unit) {
        if (identitiesMap.containsKey(identityId)) {
            onSuccess(identitiesMap[identityId]!!)
        } else {
            viewModelScope.launch {
                uiState = ManageIdentityUiState.GetIdentityLoading
                uiState = try {
                    val identity = idpRepository.getIdentity(identityId = identityId)
                    identitiesMap[identity.userId] = identity
                    onSuccess(identity)
                    ManageIdentityUiState.GetIdentitySuccess
                } catch (e: IOException) {
                    ManageIdentityUiState.Error
                } catch (e: HttpException) {
                    ManageIdentityUiState.Error
                }
            }
        }
    }

    fun getPortrait(identityId: String, onSuccess: (bitmapImage: Bitmap) -> Unit) {
        if (identitiesMap.containsKey(identityId)) {
            onSuccess(portraitMap[identityId]!!)
        } else {
            viewModelScope.launch {
                try {
                    val base64Image = idpRepository.getPortrait(identityId = identityId)
                    val byteArray = base64Image.toByteArray()
                    val bitmapImage = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                    portraitMap[identityId] = bitmapImage
                    onSuccess(bitmapImage)
                } catch (e: IOException) {
                    ManageIdentityUiState.Error
                } catch (e: HttpException) {
                    ManageIdentityUiState.Error
                }
            }
        }
    }

    fun removeIdentitiesFromGroup(
        groupId: String,
        userIds: List<String>
    ) {
        viewModelScope.launch {
            uiState = ManageIdentityUiState.RemoveIdentityFromGroupLoading
            uiState = try {
                idpRepository.removeGroupIdentities(
                    groupId = groupId,
                    request = RemoveGroupIdentitiesRequest(userIds = userIds)
                )
                getGroup(groupId)
                ManageIdentityUiState.RemoveIdentityFromGroupSuccess
            } catch (e: IOException) {
                ManageIdentityUiState.Error
            } catch (e: HttpException) {
                ManageIdentityUiState.Error
            }
        }
    }

    /**
     * Factory for [GroupSelectionViewModel] that takes [IdpRepository] as a dependency
     */
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as LobbyAppApplication)
                val idpRepository = application.container.idpRepository
                ManageIdentityViewModel(idpRepository = idpRepository)
            }
        }
    }
}