package com.example.lobbyapp.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.lobbyapp.R
import com.example.lobbyapp.model.Identity
import com.example.lobbyapp.model.IdentitySummary
import com.example.lobbyapp.ui.theme.BackgroundGreyColor
import com.example.lobbyapp.ui.theme.body4
import com.example.lobbyapp.ui.viewModel.ManageIdentityViewModel
import com.example.lobbyapp.util.pixelToSecondaryDp

@Composable
fun IdentityTable(
    manageIdentityViewModel: ManageIdentityViewModel,
    identities: List<IdentitySummary>,
    selectedIds: List<String>,
    onCheckedChange: (String) -> Unit,
    selectAll: () -> Unit,
) {
    val column1Weight = .1f
    val column2Weight = .1f
    val column3Weight = .4f
    val column4Weight = .4f

    if (identities.isEmpty()) {
        Text(
            text = stringResource(R.string.empty),
            style = MaterialTheme.typography.body4,
            modifier = Modifier.padding(top = 24.pixelToSecondaryDp)
        )
    } else {
        LazyColumn(
            Modifier
                .fillMaxWidth()
                .padding(16.pixelToSecondaryDp),
        ) {
            // Table Header
            item {
                Row(
                    Modifier
                        .background(BackgroundGreyColor)
                        .padding(vertical = 12.pixelToSecondaryDp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(32.pixelToSecondaryDp),
                ) {
                    Checkbox(
                        checked = selectedIds.size == identities.size,
                        colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colors.primary),
                        onCheckedChange = { selectAll() },
                        modifier = Modifier.weight(column1Weight)
                    )
                    TableCell(
                        text = stringResource(R.string.photo),
                        weight = column2Weight,
                        isHeader = true
                    )
                    TableCell(
                        text = stringResource(R.string.name),
                        weight = column3Weight,
                        isHeader = true
                    )
                    TableCell(
                        text = stringResource(R.string.added_date),
                        weight = column4Weight,
                        isHeader = true
                    )
                }
            }
            // Table Content.
            items(identities.size) { rowIndex ->
                val identity = identities[rowIndex]
                IdentityRow(
                    manageIdentityViewModel = manageIdentityViewModel,
                    identity = identity,
                    selectedIds = selectedIds,
                    onCheckedChange = onCheckedChange,
                    column1Weight = column1Weight,
                    column2Weight = column2Weight,
                    column3Weight = column3Weight,
                    column4Weight = column4Weight,
                )
            }
        }
    }
}

@Composable
fun IdentityRow(
    manageIdentityViewModel: ManageIdentityViewModel,
    identity: IdentitySummary,
    selectedIds: List<String>,
    onCheckedChange: (String) -> Unit,
    column1Weight: Float,
    column2Weight: Float,
    column3Weight: Float,
    column4Weight: Float,
) {
    val (identityDetails, setIdentityDetails) = remember { mutableStateOf<Identity?>(null) }
    LaunchedEffect(identity) {
        manageIdentityViewModel.getIdentity(identityId = identity.userId, setIdentityDetails)
    }
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 16.pixelToSecondaryDp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(32.pixelToSecondaryDp),
    ) {
        Checkbox(
            checked = selectedIds.contains((identity.userId)),
            colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colors.primary),
            onCheckedChange = { onCheckedChange(identity.userId) },
            modifier = Modifier.weight(column1Weight)
        )
        Box(
            modifier = Modifier
                .weight(column2Weight)
                .height(80.pixelToSecondaryDp),
            contentAlignment = Alignment.Center
        ) {
            Avatar(identityId = identity.userId, manageIdentityViewModel = manageIdentityViewModel)
        }
        TableCell(
            text = "${identity.firstName} ${identity.lastName}", weight = column3Weight
        )
        TableCell(
            text = if (identityDetails != null) "${identityDetails.createdAt}" else "",
            weight = column4Weight
        )
    }
    Divider(color = MaterialTheme.colors.secondary)
}

@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float,
    isHeader: Boolean = false,
) {
    Text(
        text = text,
        modifier = Modifier
            .weight(weight)
            .padding(8.dp),
        style = if (isHeader) MaterialTheme.typography.h4 else MaterialTheme.typography.body4
    )
}