package com.github.funczz.ruby_calc.android.event

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.github.funczz.kotlin.notifier.Notifier
import com.github.funczz.ruby_calc.android.Presenter
import com.github.funczz.ruby_calc.android.R
import com.github.funczz.ruby_calc.android.UiState
import com.github.funczz.ruby_calc.android.destination.problem.ProblemUiCommand
import com.github.funczz.ruby_calc.android.destination.program.ProgramUiCommand
import com.github.funczz.ruby_calc.android.parts.IndexOrderSelectionDropdownMenuBox
import com.github.funczz.ruby_calc.core.model.program.ProgramModel

@Suppress("Unused")
data class ProgramSelectionDialogUiEvent(
    val confirmAction: (ProgramModel) -> Unit,
    val dismissAction: () -> Unit,
    val presenter: Presenter<UiState>,
    val notifier: Notifier,
    val modifier: Modifier = Modifier,
) : UiEvent {

    override var isDone: Boolean = false

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Show() {

        if (isDone) return

        var selectedIndex by rememberSaveable { mutableIntStateOf(-1) }

        val uiState by presenter.getState()

        val labels = uiState.programSelection.programList.map { it.name }

        val values = uiState.programSelection.programList

        val onIndexValue: (String) -> Unit = {
            ProgramUiCommand.programSelection(
                value = it,
                orderColumn = uiState.programSelection.orderColumn,
                orderBy = uiState.programSelection.orderBy,
                limit = uiState.programSelection.limit,
                notifier = notifier,
            )
        }

        onIndexValue(uiState.programSelectionSearchBoxUiState.indexValue.value.text)

        Dialog(
            onDismissRequest = {},
        ) {
            Surface(
                modifier = modifier,
                shape = AlertDialogDefaults.shape,
                color = AlertDialogDefaults.containerColor,
                tonalElevation = AlertDialogDefaults.TonalElevation,
            ) {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            //検索テキストボックス
                            title = {
                                OutlinedTextField(
                                    value = uiState.programSelectionSearchBoxUiState.indexValue.value,
                                    onValueChange = { v ->
                                        onIndexValue(v.text)
                                        uiState.programSelectionSearchBoxUiState.indexValue.onValueChange(
                                            value = v
                                        )
                                    },
                                    modifier = Modifier.size(TextFieldDefaults.MinWidth),
                                    textStyle = MaterialTheme.typography.bodyMedium,
                                    label = { Text(text = stringResource(id = R.string.program_index_search_label)) },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Filled.Search,
                                            contentDescription = null,
                                        )
                                    },
                                    singleLine = true,
                                )
                            },
                            //並べ替えドロップダウンメニュー
                            navigationIcon = {
                                IndexOrderSelectionDropdownMenuBox(
                                    value = { uiState.programSelection.value },
                                    column = { uiState.programSelection.orderColumn },
                                    orderBy = { uiState.programSelection.orderBy },
                                    limit = { uiState.programSelection.limit },
                                    action = { value, column, orderBy, limit, notifier ->
                                        ProblemUiCommand.programSelection(
                                            value = value,
                                            orderColumn = column,
                                            orderBy = orderBy,
                                            limit = limit,
                                            notifier = notifier
                                        )
                                    },
                                    notifier = notifier,
                                )
                            },
                            actions = {
                                TextButton(
                                    onClick = {
                                        isDone = true
                                        dismissAction()
                                    },
                                ) {
                                    Text(
                                        stringResource(id = android.R.string.cancel)
                                    )
                                }

                                TextButton(
                                    onClick = {
                                        isDone = true
                                        confirmAction(values[selectedIndex])
                                    },
                                    enabled = selectedIndex >= 0 && selectedIndex < values.count()
                                ) {
                                    Text(
                                        stringResource(id = android.R.string.ok)
                                    )
                                }
                            }
                        )
                    }
                ) {
                    Column(
                        modifier = modifier
                            .padding(it)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {

                        LazyColumn {
                            items(labels.size) { index ->
                                val isSelected = (selectedIndex >= 0 && index == selectedIndex)
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(4.dp)
                                        .selectable(
                                            selected = isSelected,
                                            onClick = {
                                                selectedIndex = index
                                            }
                                        ),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    RadioButton(
                                        selected = isSelected,
                                        onClick = {
                                            selectedIndex = index
                                        },
                                    )
                                    Text(
                                        text = labels[index],
                                        color = AlertDialogDefaults.textContentColor,
                                        style = MaterialTheme.typography.bodyMedium,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}
