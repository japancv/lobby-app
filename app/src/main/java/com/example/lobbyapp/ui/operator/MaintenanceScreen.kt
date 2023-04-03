package com.example.lobbyapp.ui.operator

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lobbyapp.LobbyAppApplication
import com.example.lobbyapp.R
import com.example.lobbyapp.model.IdpGroupSummary
import com.example.lobbyapp.ui.component.*
import com.example.lobbyapp.ui.viewModel.ManageIdentityUiState
import com.example.lobbyapp.ui.viewModel.ManageIdentityViewModel

@Composable
fun MaintenanceScreen(
    onCancelButtonClicked: () -> Unit = {},
) {
    val application = LocalContext.current.applicationContext as LobbyAppApplication
    val manageIdentityViewModel: ManageIdentityViewModel =
        viewModel(factory = ManageIdentityViewModel.Factory)
    val (groupId, setGroupId) = remember { mutableStateOf("") }

    LaunchedEffect(true) {
        manageIdentityViewModel.getGroups()
    }

    when (manageIdentityViewModel.uiState) {
        ManageIdentityUiState.GetGroupsLoading -> LoadingDialog()
        ManageIdentityUiState.Error -> ErrorDialog(
            errorMessage = manageIdentityViewModel.error?.message,
            onConfirm = onCancelButtonClicked
        )
        else -> {
            if (groupId == "") {
                SelectionScreen(
                    manageIdentityViewModel = manageIdentityViewModel,
                    onCancelButtonClicked = onCancelButtonClicked,
                    prefix = application.container.configProperties.getProperty("PREFIX"),
                    setGroupId = setGroupId,
                )
            } else {
                ManageIdentityScreen(
                    manageIdentityViewModel = manageIdentityViewModel,
                    onCancelButtonClicked = { setGroupId("") },
                    groupId = groupId
                )
            }
        }
    }
}

@Composable
fun SelectionScreen(
    manageIdentityViewModel: ManageIdentityViewModel,
    onCancelButtonClicked: () -> Unit = {},
    prefix: String,
    setGroupId: (groupId: String) -> Unit = {},
) {
    val groups = manageIdentityViewModel.groups

    GlobalLayout(
        isSecondaryDisplay = true,
        headerTitle = stringResource(R.string.title_group_selection),
        onCancelButtonClicked = onCancelButtonClicked
    ) {
        Column(modifier = Modifier.weight(1f)) {
            AutoComplete(
                multiple = false,
                placeholder = { Text(stringResource(R.string.select_group)) },
                options = groups.map { group -> Option(group.name.replace(prefix, ""), group) },
                selectedOptions = listOf<Option>(),
                onChange = fun(selectedOptions) {
                    manageIdentityViewModel.uiState = ManageIdentityUiState.GetGroupLoading
                    setGroupId(((selectedOptions[0].value as IdpGroupSummary).id))
                })
        }
    }
}
