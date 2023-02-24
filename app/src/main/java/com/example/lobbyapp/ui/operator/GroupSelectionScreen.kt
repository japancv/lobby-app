package com.example.lobbyapp.ui.operator

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lobbyapp.LobbyAppApplication
import com.example.lobbyapp.R
import com.example.lobbyapp.model.IdpGroupSummary
import com.example.lobbyapp.ui.component.*
import com.example.lobbyapp.ui.theme.body4
import com.example.lobbyapp.ui.viewModel.GroupSelectionUiState
import com.example.lobbyapp.ui.viewModel.GroupSelectionViewModel
import com.example.lobbyapp.ui.viewModel.UserInfoViewModel
import com.example.lobbyapp.util.toSecondarySp

@Composable
fun GroupSelectionScreen(
    onCancelButtonClicked: () -> Unit = {},
    onIssueButtonClicked: () -> Unit = {},
) {
    val application = LocalContext.current.applicationContext as LobbyAppApplication
    val groupSelectionViewModel: GroupSelectionViewModel =
        viewModel(factory = GroupSelectionViewModel.Factory)

    GlobalLayout(
        headerTitle = stringResource(R.string.title_group_selection),
        onCancelButtonClicked = onCancelButtonClicked
    ) {
        when (groupSelectionViewModel.uiState) {
            is GroupSelectionUiState.Loading -> LoadingScreen()
            is GroupSelectionUiState.Error -> ErrorDialog(onConfirm = onCancelButtonClicked)
            else -> GrantAccessScreen(
                groupSelectionViewModel = groupSelectionViewModel,
                onIssueButtonClicked = onIssueButtonClicked,
                prefix = application.container.configProperties.getProperty("PREFIX")
            )
        }
    }
}

@Composable
fun GrantAccessScreen(
    groupSelectionViewModel: GroupSelectionViewModel,
    onIssueButtonClicked: () -> Unit = {},
    prefix: String
) {
    val groups = groupSelectionViewModel.groups
    val selectedOptionsState = remember {
        mutableStateOf(groups.filter {
            UserInfoViewModel.uiState.value.groups.contains(it.id)
        }.map {
            Option(
                label = it.name.replace(prefix, ""),
                value = it
            )
        })
    }

    fun handleClickIssue() {
        groupSelectionViewModel.addIdentityToGroups(
            groupIds = selectedOptionsState.value.map { (it.value as IdpGroupSummary).id },
            userId = UserInfoViewModel.uiState.value.id
        )

        onIssueButtonClicked()
    }

    if (groupSelectionViewModel.uiState === GroupSelectionUiState.AddLoading) {
        LoadingDialog()
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        Column(modifier = Modifier.weight(1f)) {
            AutoComplete(
                multiple = true,
                placeholder = { Text(stringResource(R.string.select_group)) },
                options = groups.map {
                    Option(
                        label = it.name.replace(prefix, ""),
                        value = it
                    )
                },
                selectedOptions = selectedOptionsState.value,
                onChange = fun(selectedOptions) {
                    selectedOptionsState.value = selectedOptions
                })
        }
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.SpaceBetween) {
            if (selectedOptionsState.value.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .weight(1f, false)
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    selectedOptionsState.value.forEach { option ->
                        SelectedGroupItem(
                            group = option.value as IdpGroupSummary,
                            prefix = prefix,
                            onDelete = fun(
                                deletedGroup
                            ) {
                                selectedOptionsState.value =
                                    selectedOptionsState.value.filter { it.value !== deletedGroup }
                            })
                    }
                }
            }

            CustomButton(
                modifier = Modifier.fillMaxWidth(),
                disabled = selectedOptionsState.value.isEmpty(),
                buttonText = stringResource(R.string.issue),
                onClick = { handleClickIssue() }
            )
        }
    }
}

@Composable
fun SelectedGroupItem(
    group: IdpGroupSummary,
    onDelete: (group: IdpGroupSummary) -> Unit,
    prefix: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            style = MaterialTheme.typography.body4.toSecondarySp(),
            text = group.name.replace(prefix, "")
        )
        IconButton(
            onClick = fun() { onDelete(group) },
        ) {
            Icon(Icons.Rounded.Close, "Delete group")
        }
    }
}