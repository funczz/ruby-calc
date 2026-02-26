package com.github.funczz.ruby_calc.android.destination.program

import android.annotation.SuppressLint
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Redo
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.github.funczz.kotlin.notifier.Notifier
import com.github.funczz.ruby_calc.android.MainApplication
import com.github.funczz.ruby_calc.android.Presenter
import com.github.funczz.ruby_calc.android.R
import com.github.funczz.ruby_calc.android.UiState
import com.github.funczz.ruby_calc.android.event.CloseDialogUiEvent
import com.github.funczz.ruby_calc.android.event.SaveDialogUiEvent
import com.github.funczz.ruby_calc.android.event.TextViewerDialogUiEvent
import com.github.funczz.ruby_calc.android.parts.CalculatorBox

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgramEditScreen(
    presenter: Presenter<UiState>,
    notifier: Notifier,
    navHostController: NavHostController,
    modifier: Modifier,
) {
    val title = MainApplication.SCRIPT_PATH

    val body = MainApplication.scriptText

    val uiState by presenter.getState()

    ProgramUiCommand.clearSaveResult(presenter = presenter, notifier = notifier)

    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )

    val onShow: () -> Unit = { showBottomSheet = true }

    val onSave: () -> Unit = {
        val saveDialogUiEvent = SaveDialogUiEvent(
            confirmAction = {
                ProgramUiCommand.save(
                    presenter = presenter,
                    notifier = notifier,
                )
                ProgramUiCommand.delUiEvent(presenter = presenter)
            },
            dismissAction = {
                ProgramUiCommand.delUiEvent(presenter = presenter)
            }
        )
        ProgramUiCommand.addUiEvent(uiEvent = saveDialogUiEvent, presenter = presenter)
    }

    val onCancel: () -> Unit = {
        val closeDialogUiEvent = CloseDialogUiEvent(
            confirmAction = {
                navHostController.navigateUp()
                ProgramUiCommand.delUiEvent(presenter = presenter)
            },
            dismissAction = {
                ProgramUiCommand.delUiEvent(presenter = presenter)
            }
        )
        ProgramUiCommand.addUiEvent(uiEvent = closeDialogUiEvent, presenter = presenter)
    }

    Dialog(onDismissRequest = {}) {
        Surface(
            modifier = modifier.fillMaxSize(),
            shape = RoundedCornerShape(0.dp),
            color = Color.White
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Scaffold(
                    bottomBar = {
                        BottomAppBar(
                            actions = {
                                IconButton(
                                    onClick = {
                                        if (uiState.programEditUiState.code.canUndo()) {
                                            uiState.programEditUiState.code.undo()
                                        }
                                    },
                                ) {
                                    Icon(
                                        Icons.AutoMirrored.Filled.Undo,
                                        contentDescription = stringResource(id = R.string.undo_content_description)
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        if (uiState.programEditUiState.code.canRedo()) {
                                            uiState.programEditUiState.code.redo()
                                        }
                                    },
                                ) {
                                    Icon(
                                        Icons.AutoMirrored.Filled.Redo,
                                        contentDescription = stringResource(id = R.string.redo_content_description)
                                    )
                                }

                            },
                        )
                    },
                    topBar = {
                        TopAppBar(
                            title = {
                                ProgramEditDropdownMenuBox(
                                    title = title,
                                    body = body,
                                    presenter = presenter,
                                    modifier = modifier,
                                )
                            },
                            navigationIcon = {
                                IconButton(onClick = onCancel) {
                                    Icon(
                                        Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = stringResource(id = R.string.back_content_description)
                                    )
                                }
                            },
                            actions = {
                                Button(
                                    onClick = onShow,
                                    colors = ButtonDefaults.buttonColors(
                                        contentColor = colorResource(id = R.color.bootstrap_dark),
                                        containerColor = colorResource(id = R.color.bootstrap_warning),
                                        disabledContentColor = colorResource(id = R.color.bootstrap_light),
                                        disabledContainerColor = colorResource(id = R.color.bootstrap_secondary),
                                    )
                                ) {
                                    Icon(
                                        Icons.Filled.Calculate,
                                        contentDescription = stringResource(id = R.string.calculator_open_label),
                                    )
                                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                                    Text(stringResource(id = R.string.calculator_open_label))
                                }
                                Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                                Button(
                                    onClick = onSave,
                                    enabled = uiState.programEditUiState.name.value.text.isNotBlank(),
                                    colors = ButtonDefaults.buttonColors(
                                        contentColor = colorResource(id = R.color.bootstrap_light),
                                        containerColor = colorResource(id = R.color.bootstrap_success),
                                        disabledContentColor = colorResource(id = R.color.bootstrap_light),
                                        disabledContainerColor = colorResource(id = R.color.bootstrap_secondary),
                                    )
                                ) {
                                    Icon(
                                        Icons.Filled.Save,
                                        contentDescription = stringResource(id = R.string.save_content_description),
                                        modifier = Modifier.size(ButtonDefaults.IconSize)
                                    )
                                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                                    Text(stringResource(id = R.string.save_content_description))
                                }
                            }
                        )
                    }) {
                    if (showBottomSheet) {
                        ModalBottomSheet(
                            modifier = Modifier.fillMaxHeight(),
                            sheetState = sheetState,
                            onDismissRequest = { showBottomSheet = false }
                        ) {
                            CalculatorBox(
                                code = uiState.programEditUiState.code.value.text,
                                hint = uiState.programEditUiState.hint.value.text,
                                problemId = null,
                                uiCommand = ProgramUiCommand,
                                presenter = presenter,
                                notifier = notifier,
                            )
                        }
                    }

                    Column(
                        modifier = modifier
                            .padding(it)
                            .fillMaxSize()
                            .horizontalScroll(rememberScrollState())
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        TextField(
                            value = uiState.programEditUiState.name.value,
                            onValueChange = { v ->
                                uiState.programEditUiState.name.onValueChange(value = v)
                            },
                            modifier = Modifier
                                .padding(20.dp)
                                .align(Alignment.Start),
                            label = { Text(text = stringResource(id = R.string.program_name_label)) },
                            isError = uiState.programSaveResult.existsName,
                            supportingText = {
                                if (uiState.programSaveResult.existsName) {
                                    Text(
                                        modifier = Modifier.fillMaxWidth(),
                                        text = stringResource(id = R.string.program_name_supporting_already_exists),
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                            },
                        )
                        TextField(
                            value = uiState.programEditUiState.description.value,
                            onValueChange = { v ->
                                uiState.programEditUiState.description.onValueChange(value = v)
                            },
                            modifier = Modifier
                                .padding(20.dp)
                                .align(Alignment.Start),
                            singleLine = false,
                            label = { Text(text = stringResource(id = R.string.program_description_label)) },
                        )
                        TextField(
                            value = uiState.programEditUiState.hint.value,
                            onValueChange = { v ->
                                uiState.programEditUiState.hint.onValueChange(value = v)
                            },
                            modifier = Modifier
                                .padding(20.dp)
                                .align(Alignment.Start),
                            singleLine = false,
                            label = { Text(text = stringResource(id = R.string.program_hint_label)) },
                        )
                        TextField(
                            value = uiState.programEditUiState.code.value,
                            onValueChange = { v ->
                                uiState.programEditUiState.code.onValueChange(value = v)
                            },
                            modifier = Modifier
                                .padding(20.dp)
                                .align(Alignment.Start),
                            singleLine = false,
                            label = { Text(text = stringResource(id = R.string.program_code_label)) },
                        )
                    }
                }
            }
        }
    }

    ProgramUiCommand.ConsumeUiEvent(presenter = presenter, context = LocalContext.current)
}

@Composable
fun ProgramEditDropdownMenuBox(
    title: String,
    body: String,
    presenter: Presenter<UiState>,
    modifier: Modifier = Modifier,
) {

    var expanded by remember { mutableStateOf(false) }

    val onSelect: (() -> Unit) -> Unit = {
        it()
        expanded = false
    }

    Box(
        modifier = Modifier.wrapContentSize(Alignment.TopStart)
    ) {
        IconButton(onClick = { expanded = true }) {
            Icon(
                Icons.Filled.Menu,
                contentDescription = stringResource(id = R.string.menu_content_description),
            )
        }
        DropdownMenu(
            modifier = Modifier.clip(RoundedCornerShape(16.dp)),
            expanded = expanded,
            onDismissRequest = { onSelect {} },
        ) {
            DropdownMenuItem(
                text = { Text(text = stringResource(id = R.string.program_menu_view_ruby_script_label)) },
                onClick = {
                    onSelect {

                        ProgramUiCommand.addUiEvent(
                            uiEvent = TextViewerDialogUiEvent(
                                title = title,
                                body = body,
                                confirmAction = { ProgramUiCommand.delUiEvent(presenter = presenter) },
                                modifier = modifier,
                            ),
                            presenter = presenter
                        )
                    }
                },
            )
        }
    }
}
