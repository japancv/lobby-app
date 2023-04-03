package com.example.lobbyapp.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.example.lobbyapp.R
import com.example.lobbyapp.ui.theme.*

data class Option(
    val label: String,
    val value: Any,
    val disabled: Boolean = false,
)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AutoComplete(
    placeholder: @Composable (() -> Unit)? = null,
    options: List<Option>,
    selectedOptions: List<Option>,
    modifier: Modifier = Modifier,
    multiple: Boolean = false,
    onChange: (options: List<Option>) -> Unit
) {
    val (expanded, setExpanded) = remember { mutableStateOf(true) }
    val focusManager = LocalFocusManager.current

    fun handleSelectOption(selectedOption: Option) {
        var tmpSelectedOptions = selectedOptions

        if (multiple) {
            if (tmpSelectedOptions.contains(selectedOption)) {
                tmpSelectedOptions -= selectedOption
            } else {
                tmpSelectedOptions += selectedOption
            }
        } else {
            tmpSelectedOptions = listOf(selectedOption)
        }

        onChange(tmpSelectedOptions)
    }

    ExposedDropdownMenuBox(
        modifier = modifier.fillMaxWidth(),
        expanded = expanded,
        onExpandedChange = {
            setExpanded(!it)
        }
    ) {
        OutlinedTextField(
            value = "",
            shape = RoundedCornerShape(12.dp),
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
            placeholder = placeholder,
            trailingIcon = {
                Icon(Icons.Rounded.KeyboardArrowDown, "")
            },
            onValueChange = {},
        )
        MaterialTheme(
            shapes = MaterialTheme.shapes.copy(medium = RoundedCornerShape(8.dp)),
        ) {
            DropdownMenu(
                modifier = Modifier.exposedDropdownSize(),
                offset = DpOffset(0.dp, 4.dp),
                expanded = expanded,
                onDismissRequest = {
                    setExpanded(false)
                    focusManager.clearFocus()
                }
            ) {
                if (options.isEmpty()) {
                    DropdownMenuItem(onClick = fun() {}) {
                        Text(
                            stringResource(R.string.no_options),
                            color = MaterialTheme.colors.secondary,
                            style = MaterialTheme.typography.body4
                        )
                    }
                } else {
                    options.forEach { option ->
                        if (multiple) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(4.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Checkbox(
                                    enabled = !option.disabled,
                                    checked = selectedOptions.contains(option),
                                    colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colors.primary),
                                    onCheckedChange = fun(_) { handleSelectOption(option) })
                                Text(
                                    text = option.label,
                                    style = MaterialTheme.typography.body5,
                                    color = if (option.disabled) MaterialTheme.colors.secondary else MaterialTheme.colors.secondaryVariant
                                )
                            }
                        } else {
                            DropdownMenuItem(
                                enabled = !option.disabled,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(4.dp),
                                onClick = {
                                    handleSelectOption(option)
                                    focusManager.clearFocus()
                                }
                            ) {
                                Text(
                                    option.label,
                                    style = MaterialTheme.typography.body5,
                                    color = if (option.disabled) MaterialTheme.colors.secondary else MaterialTheme.colors.secondaryVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AutoCompletePreview() {
    val (selectedOptions, setSelectedOptions) = remember { mutableStateOf(listOf<Option>()) }

    LobbyAppTheme {
        Box(
            modifier = Modifier.padding(8.dp)
        ) {
            AutoComplete(
                multiple = true,
                placeholder = { Text("Select") },
                options = listOf(
                    Option("Option 1", "Value 1"),
                    Option("Option 2", "Value 2"),
                    Option("Option 3", "Value 3")
                ),
                selectedOptions = selectedOptions,
                onChange = {
                    setSelectedOptions(it)
                }
            )
        }
    }
}