package com.example.lobbyapp.ui.viewModel

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
import com.example.lobbyapp.model.AddGroupIdentitiesRequest
import com.example.lobbyapp.model.IdpGroupSummary
import com.example.lobbyapp.model.RemoveGroupIdentitiesRequest
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.io.IOException

sealed interface GroupSelectionUiState {
    object Success : GroupSelectionUiState
    object AddSuccess : GroupSelectionUiState
    object Error : GroupSelectionUiState
    object Loading : GroupSelectionUiState
    object AddLoading : GroupSelectionUiState
}

class GroupSelectionViewModel(private val idpRepository: IdpRepository) : ViewModel() {
    var uiState: GroupSelectionUiState by mutableStateOf(GroupSelectionUiState.Loading)
    var groups by mutableStateOf(listOf<IdpGroupSummary>())

    init {
        getIdpGroups()
    }

    private fun getIdpGroups() {
        viewModelScope.launch {
            uiState = GroupSelectionUiState.Loading
            uiState = try {
                groups =
                    withContext(Dispatchers.Default) { idpRepository.getGroups() }.groups
                GroupSelectionUiState.Success
            } catch (e: IOException) {
                GroupSelectionUiState.Error
            } catch (e: HttpException) {
                GroupSelectionUiState.Error
            }
        }
    }

    fun addIdentityToGroups(groupIds: List<String>, userId: String) {
        viewModelScope.launch {
            val userIds = listOf(
                UserInfoViewModel.uiState.value.id
            )
            uiState = GroupSelectionUiState.AddLoading
            uiState = try {
                // remove the user from the unselected groups
                groups.filter { !groupIds.contains(it.id) }.map {
                    async {
                        idpRepository.removeGroupIdentities(
                            groupId = it.id,
                            request = RemoveGroupIdentitiesRequest(
                                userIds = userIds
                            )
                        )
                    }
                }.awaitAll()
                // add the user to the selected group
                groupIds.map {
                    async {
                        idpRepository.addGroupIdentities(
                            groupId = it,
                            request = AddGroupIdentitiesRequest(
                                userIds = userIds
                            )
                        )
                    }
                }.awaitAll()

                GroupSelectionUiState.AddSuccess
            } catch (e: IOException) {
                GroupSelectionUiState.Error
            } catch (e: HttpException) {
                GroupSelectionUiState.Error
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
                GroupSelectionViewModel(idpRepository = idpRepository)
            }
        }
    }
}