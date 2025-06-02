package com.github.funczz.ruby_calc.android.event

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.github.funczz.ruby_calc.android.ui.theme.RubyCalcTheme

@Suppress("Unused")
data class SingleSelectionDialogUiEvent<T>(
    val labelList: () -> List<String>,
    val valueList: () -> List<T>,
    val defaultSelectedIndex: () -> Int,
    val confirmAction: (T) -> Unit,
    val dismissAction: () -> Unit,
    val title: @Composable () -> Unit,
    val message: @Composable () -> Unit,
    val modifier: Modifier = Modifier,
    val confirmButtonLabel: String? = null,
    val dismissButtonLabel: String? = null,
) : UiEvent {

    override var isDone: Boolean = false

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Show() {
        if (isDone) return
        var selectedIndex by rememberSaveable { mutableIntStateOf(defaultSelectedIndex()) }
        val labels = labelList()
        val values = valueList()
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
                            title = title,
                            navigationIcon = {},
                            actions = {
                                TextButton(
                                    onClick = {
                                        isDone = true
                                        dismissAction()
                                    },
                                ) {
                                    Text(
                                        dismissButtonLabel
                                            ?: stringResource(id = android.R.string.cancel)
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
                                        confirmButtonLabel
                                            ?: stringResource(id = android.R.string.ok)
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

                        message()

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

@Preview(showBackground = true)
@Composable
fun EnableConfirmButtonSingleSelectionDialogUiEventPreview() {
    RubyCalcTheme {
        SingleSelectionDialogUiEvent(
            labelList = { (0..99).map { "item $it" } },
            valueList = { (0..99).map { it } },
            defaultSelectedIndex = { 0 },
            confirmAction = {},
            dismissAction = {},
            title = { Text(text = "Title") },
            message = { Text(text = "Message.") },
        ).Show()
    }
}

@Preview(showBackground = true)
@Composable
fun DisableConfirmButtonSingleSelectionDialogUiEventPreview() {
    RubyCalcTheme {
        SingleSelectionDialogUiEvent(
            labelList = { (0..99).map { "item $it" } },
            valueList = { (0..99).map { it } },
            defaultSelectedIndex = { -1 },
            confirmAction = {},
            dismissAction = {},
            title = { Text(text = "Title") },
            message = { Text(text = "Message.") },
        ).Show()
    }
}
