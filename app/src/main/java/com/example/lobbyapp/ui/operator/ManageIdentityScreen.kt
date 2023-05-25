package com.example.lobbyapp.ui.operator

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.lobbyapp.R
import com.example.lobbyapp.ui.component.ConfirmDialog
import com.example.lobbyapp.ui.component.CustomButton
import com.example.lobbyapp.ui.component.GlobalLayout
import com.example.lobbyapp.ui.component.IdentityTable
import com.example.lobbyapp.ui.component.LoadingDialog
import com.example.lobbyapp.ui.viewModel.ManageIdentityUiState
import com.example.lobbyapp.ui.viewModel.ManageIdentityViewModel
import kotlinx.coroutines.launch

@Composable
fun ManageIdentityScreen(
    manageIdentityViewModel: ManageIdentityViewModel,
    onCancelButtonClicked: () -> Unit = {},
    groupId: String,
) {
    val coroutineScope = rememberCoroutineScope()
    val showConfirmDialog = remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        manageIdentityViewModel.getGroup(groupId = groupId)
    }

    if (manageIdentityViewModel.uiState === ManageIdentityUiState.RemoveIdentityFromGroupLoading ||
        manageIdentityViewModel.uiState === ManageIdentityUiState.GetGroupLoading
    ) {
        LoadingDialog()
    }

    GlobalLayout(
        isSecondaryDisplay = true,
        isTableView = true,
        headerBottomBorder = false,
        onCancelButtonClicked = onCancelButtonClicked,
    ) {
        if (showConfirmDialog.value) {
            ConfirmDialog(
                onConfirm = {
                    coroutineScope.launch {
                        manageIdentityViewModel.removeIdentitiesFromGroup(
                            groupId = groupId,
                            userIds = manageIdentityViewModel.identitySummaries.map { identity -> identity.userId }
                        )
                    }
                    showConfirmDialog.value = false
                },
                onCancel = {
                    showConfirmDialog.value = false
                },
                title = stringResource(R.string.confirm_action),
                text = stringResource(R.string.confirm_delete_all)
            )
        }
        Column(modifier = Modifier.weight(.8f)) {
            IdentityTable(
                manageIdentityViewModel = manageIdentityViewModel,
                identities = manageIdentityViewModel.identitySummaries,
                selectedIds = manageIdentityViewModel.selectedIds,
                onCheckedChange = { selectedId ->
                    manageIdentityViewModel.updateSelectedIds(
                        selectedId
                    )
                },
                selectAll = { manageIdentityViewModel.selectAll() })
        }
        Column(modifier = Modifier.weight(.2f)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 100.dp, end = 100.dp)
            ) {
                CustomButton(
                    buttonText = stringResource(R.string.remove),
                    disabled = manageIdentityViewModel.selectedIds.isEmpty(),
                    onClick = {
                        coroutineScope.launch {
                            manageIdentityViewModel.removeIdentitiesFromGroup(
                                groupId = groupId,
                                userIds = manageIdentityViewModel.selectedIds
                            )
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
                CustomButton(
                    buttonText = stringResource(R.string.remove_all),
                    background = MaterialTheme.colors.background,
                    textColor = if (manageIdentityViewModel.selectedIds.isEmpty()) Color.White else MaterialTheme.colors.primary,
                    border = BorderStroke(
                        1.dp,
                        if (manageIdentityViewModel.selectedIds.isEmpty()) Color.White else MaterialTheme.colors.secondary
                    ),
                    disabled = manageIdentityViewModel.selectedIds.isEmpty(),
                    onClick = {
                        showConfirmDialog.value = true
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}