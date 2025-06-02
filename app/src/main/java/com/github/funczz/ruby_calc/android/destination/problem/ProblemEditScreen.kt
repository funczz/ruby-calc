package com.github.funczz.ruby_calc.android.destination.problem

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.github.funczz.kotlin.notifier.Notifier
import com.github.funczz.ruby_calc.android.Presenter
import com.github.funczz.ruby_calc.android.R
import com.github.funczz.ruby_calc.android.UiState
import com.github.funczz.ruby_calc.android.event.CloseDialogUiEvent
import com.github.funczz.ruby_calc.android.event.ProgramSelectionDialogUiEvent
import com.github.funczz.ruby_calc.android.event.SaveDialogUiEvent
import com.github.funczz.ruby_calc.android.parts.CalculatorBox
import com.github.funczz.ruby_calc.core.model.program.ProgramModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProblemEditScreen(
    presenter: Presenter<UiState>,
    notifier: Notifier,
    navHostController: NavHostController,
    modifier: Modifier,
    isNewProblem: Boolean = false,
) {
    ProblemUiCommand.clearSaveResult(presenter = presenter, notifier = notifier)

    val uiState by presenter.getState()

    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )

    val onShow: () -> Unit = { showBottomSheet = true }

    val onSave: () -> Unit = {
        val saveDialogUiEvent = SaveDialogUiEvent(
            confirmAction = {
                ProblemUiCommand.save(presenter = presenter, notifier = notifier)
                ProblemUiCommand.delUiEvent(presenter = presenter)
            },
            dismissAction = {
                ProblemUiCommand.delUiEvent(presenter = presenter)
            }
        )
        ProblemUiCommand.addUiEvent(uiEvent = saveDialogUiEvent, presenter = presenter)
    }

    val onCancel: () -> Unit = {
        val closeDialogUiEvent = CloseDialogUiEvent(
            confirmAction = {
                ProblemUiCommand.close(notifier = notifier, navHostController = navHostController)
                if (isNewProblem && uiState.problemEditUiState.id is Long) {
                    ProblemUiCommand.show(navHostController = navHostController)
                }
                ProblemUiCommand.delUiEvent(presenter = presenter)
            },
            dismissAction = {
                ProblemUiCommand.delUiEvent(presenter = presenter)
            }
        )
        ProblemUiCommand.addUiEvent(uiEvent = closeDialogUiEvent, presenter = presenter)
    }

    val onSelect: () -> Unit = {
        val selectionDialogUiEvent = ProgramSelectionDialogUiEvent(
            confirmAction = {
                presenter.render(
                    output = uiState.copy(
                        problemEditUiState = uiState.problemEditUiState.copy(
                            programModel = it
                        )
                    )
                )
                ProblemUiCommand.delUiEvent(presenter = presenter)
            },
            dismissAction = {
                ProblemUiCommand.delUiEvent(presenter = presenter)
            },
            presenter = presenter,
            notifier = notifier,
            modifier = modifier,
        )
        ProblemUiCommand.addUiEvent(uiEvent = selectionDialogUiEvent, presenter = presenter)
    }

    ProblemUiCommand.programSelection(
        value = uiState.programSelection.value,
        orderColumn = uiState.programSelection.orderColumn,
        orderBy = uiState.programSelection.orderBy,
        limit = uiState.programSelection.limit,
        notifier = notifier,
    )

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
                    topBar = {
                        TopAppBar(
                            title = {},
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
                                    enabled = uiState.problemEditUiState.programModel is ProgramModel,
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
                                    enabled = uiState.problemEditUiState.name.value.text.isNotBlank()
                                            && uiState.problemEditUiState.programModel is ProgramModel,
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
                    if (showBottomSheet && uiState.problemEditUiState.programModel is ProgramModel) {
                        ModalBottomSheet(
                            modifier = Modifier.fillMaxHeight(),
                            sheetState = sheetState,
                            onDismissRequest = { showBottomSheet = false }
                        ) {
                            CalculatorBox(
                                code = uiState.problemEditUiState.programModel!!.code,
                                hint = uiState.problemEditUiState.programModel!!.hint,
                                problemId = uiState.problemEditUiState.id,
                                uiCommand = ProblemUiCommand,
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
                            value = uiState.problemEditUiState.name.value,
                            onValueChange = { v ->
                                uiState.problemEditUiState.name.onValueChange(value = v)
                            },
                            modifier = Modifier
                                .padding(20.dp)
                                .align(Alignment.Start),
                            label = { Text(text = stringResource(id = R.string.problem_name_label)) },
                            isError = uiState.problemSaveResult.existsName,
                            supportingText = {
                                if (uiState.problemSaveResult.existsName) {
                                    Text(
                                        modifier = Modifier.fillMaxWidth(),
                                        text = stringResource(id = R.string.problem_name_supporting_already_exists),
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                            },
                        )
                        Button(
                            onClick = onSelect,
                            modifier = modifier
                                .padding(start = 20.dp)
                                .align(Alignment.Start),
                            colors = ButtonDefaults.buttonColors(
                                contentColor = colorResource(id = R.color.bootstrap_light),
                                containerColor = colorResource(id = R.color.bootstrap_primary),
                                disabledContentColor = colorResource(id = R.color.bootstrap_light),
                                disabledContainerColor = colorResource(id = R.color.bootstrap_secondary),
                            )
                        ) {
                            Icon(
                                Icons.Filled.Code,
                                contentDescription = stringResource(id = R.string.program_destination_label),
                            )
                            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                            Text(stringResource(id = R.string.program_destination_label))
                        }
                        OutlinedTextField(
                            value = uiState.problemEditUiState.programModel?.name ?: "",
                            onValueChange = {},
                            modifier = Modifier
                                .padding(20.dp)
                                .align(Alignment.Start)
                                .clickable(
                                    onClick = onSelect
                                ),
                            readOnly = true,
                            singleLine = true,
                            label = { Text(text = stringResource(id = R.string.problem_program_name_label)) },
                        )
                        TextField(
                            value = uiState.problemEditUiState.comment.value,
                            onValueChange = { v ->
                                uiState.problemEditUiState.comment.onValueChange(value = v)
                            },
                            modifier = Modifier
                                .padding(20.dp)
                                .align(Alignment.Start),
                            singleLine = false,
                            label = { Text(text = stringResource(id = R.string.problem_comment_label)) },
                        )
                    }
                }
            }
        }
    }

    ProblemUiCommand.ConsumeUiEvent(presenter = presenter, context = LocalContext.current)
}
