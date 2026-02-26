package com.github.funczz.ruby_calc.android.parts

import android.annotation.SuppressLint
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Start
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.github.funczz.kotlin.notifier.Notifier
import com.github.funczz.ruby_calc.android.NewlineMarkersVisualTransformation
import com.github.funczz.ruby_calc.android.Presenter
import com.github.funczz.ruby_calc.android.R
import com.github.funczz.ruby_calc.android.TextFieldState
import com.github.funczz.ruby_calc.android.UiCommand
import com.github.funczz.ruby_calc.android.UiState
import com.github.funczz.ruby_calc.android.destination.problem.ProblemUiCommand
import com.github.funczz.ruby_calc.android.destination.program.ProgramUiCommand
import com.github.funczz.ruby_calc.android.event.SaveDialogUiEvent
import com.github.funczz.ruby_calc.android.newlineMarker
import kotlin.jvm.optionals.getOrNull

@SuppressLint("MutableCollectionMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorBox(
    code: String,
    hint: String,
    problemId: Long?,
    uiCommand: UiCommand,
    presenter: Presenter<UiState>,
    notifier: Notifier,
    modifier: Modifier = Modifier,
    newLineMakerString: String = NewlineMarkersVisualTransformation.NEWLINE_MAKER_STRING,
    newLineMakerSpanStyle: SpanStyle = NewlineMarkersVisualTransformation.NEWLINE_MAKER_SPAN_STYLE,
) {
    ProgramUiCommand.clearSaveResult(presenter = presenter, notifier = notifier)
    ProblemUiCommand.clearSaveResult(presenter = presenter, notifier = notifier)

    val uiState by presenter.getState()

    var argv: List<String> by remember {
        mutableStateOf(uiState.argvUiState.toARGV())
    }

    val result = uiState.rubyResult.failure.getOrNull()?.stackTraceToString()
        ?: uiState.rubyResult.success

    val onAddARGV: () -> Unit = {
        uiState.argvUiState.argv.add(TextFieldState(text = "", undoManager = null))
        argv = uiState.argvUiState.toARGV()
    }

    val onRemoveAtARGV: (Int) -> Unit = {
        uiState.argvUiState.argv.removeAt(it)
        argv = uiState.argvUiState.toARGV()
    }

    val onValueARGV: (Int) -> TextFieldValue = {
        uiState.argvUiState.argv[it].value
    }

    val onValueChangeARGV: (Int, TextFieldValue) -> Unit = { i, v ->
        uiState.argvUiState.argv[i].onValueChange(value = v)
        argv = uiState.argvUiState.toARGV()
    }

    val onSubmit: () -> Unit = {
        uiCommand.calculate(
            code = code,
            argv = uiState.argvUiState.toARGV(),
            notifier = notifier
        )
    }

    val onSaveARGV: () -> Unit = {
        if (problemId is Long) {
            val saveDialogUiEvent = SaveDialogUiEvent(
                confirmAction = {
                    uiCommand.saveARGV(
                        argv = uiState.argvUiState.toARGV(),
                        problemId = problemId,
                        notifier = notifier
                    )
                    uiCommand.delUiEvent(presenter = presenter)
                },
                dismissAction = {
                    uiCommand.delUiEvent(presenter = presenter)
                }
            )
            uiCommand.addUiEvent(uiEvent = saveDialogUiEvent, presenter = presenter)
        }
    }

    val onSaveResult: () -> Unit = {
        if (problemId is Long) {
            val saveDialogUiEvent = SaveDialogUiEvent(
                confirmAction = {
                    uiCommand.saveResult(
                        result = result,
                        problemId = problemId,
                        notifier = notifier
                    )
                    uiCommand.delUiEvent(presenter = presenter)
                },
                dismissAction = {
                    uiCommand.delUiEvent(presenter = presenter)
                }
            )
            uiCommand.addUiEvent(uiEvent = saveDialogUiEvent, presenter = presenter)
        }
    }

    val rubyResultTextLabel = when (uiState.rubyResult.failure.isPresent) {
        true -> stringResource(id = R.string.calculator_failure_result_label) //"SUBMIT FAILED"
        else -> stringResource(id = R.string.calculator_success_result_label) //"RESULT"
    }

    val rubyResultTextValue = uiState.rubyResult.failure.getOrNull()?.stackTraceToString()
        ?: uiState.rubyResult.success.newlineMarker(newLineMakerString)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(20.dp),
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Button(
                            onClick = onAddARGV,
                            colors = ButtonDefaults.buttonColors(
                                contentColor = colorResource(id = R.color.bootstrap_light),
                                containerColor = colorResource(id = R.color.bootstrap_primary),
                                disabledContentColor = colorResource(id = R.color.bootstrap_light),
                                disabledContainerColor = colorResource(id = R.color.bootstrap_secondary),
                            )
                        ) {
                            Icon(
                                Icons.Filled.AddCircleOutline,
                                contentDescription = stringResource(id = R.string.add_content_description),
                                modifier = Modifier.size(ButtonDefaults.IconSize)
                            )
                            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                            Text(stringResource(id = R.string.add_content_description))
                        }
                    },
                    modifier = modifier,
                    actions = {
                        Button(
                            onClick = onSubmit,
                            colors = ButtonDefaults.buttonColors(
                                contentColor = colorResource(id = R.color.bootstrap_dark),
                                containerColor = colorResource(id = R.color.bootstrap_warning),
                                disabledContentColor = colorResource(id = R.color.bootstrap_light),
                                disabledContainerColor = colorResource(id = R.color.bootstrap_secondary),
                            )
                        ) {
                            Icon(
                                Icons.Filled.Start,
                                contentDescription = stringResource(id = R.string.calculator_submit_label),
                                modifier = Modifier.size(ButtonDefaults.IconSize)
                            )
                            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                            Text(stringResource(id = R.string.calculator_submit_label))
                        }

                        if (problemId is Long) {
                            Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                            Button(
                                onClick = onSaveARGV,
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
                    },
                )
            }
        ) {
            LazyColumn(
                modifier = modifier
                    .padding(it)
                    .fillMaxSize()
                    .horizontalScroll(rememberScrollState()),
            ) {
                item {
                    if (hint.isNotBlank()) {
                        OutlinedTextField(
                            value = hint,
                            onValueChange = {},
                            modifier = modifier.fillMaxWidth(),
                            readOnly = true,
                            singleLine = false,
                            label = { Text(text = stringResource(id = R.string.program_hint_label)) },
                        )
                        Spacer(modifier = modifier.height(4.dp))
                    }
                }

                items(argv.size) { index ->
                    val label = stringResource(
                        id = R.string.calculator_argument_format,
                        index
                    )

                    Row {
                        IconButton(
                            onClick = { onRemoveAtARGV(index) },
                        ) {
                            Icon(
                                Icons.Filled.DeleteOutline,
                                contentDescription = stringResource(id = R.string.delete_content_description)
                            )
                        }

                        TextField(
                            value = onValueARGV(index),
                            onValueChange = { v ->
                                onValueChangeARGV(index, v)
                            },
                            modifier = Modifier
                                .fillMaxWidth(),
                            label = { Text(text = label) },
                            singleLine = true,
                        )
                    }
                }

                item {
                    Box(Modifier.padding(16.dp))
                }

                item {
                    Column(
                        modifier = modifier
                            .fillMaxWidth()
                    ) {
                        if (problemId is Long && result.isNotBlank()) {
                            Button(
                                onClick = onSaveResult,
                                modifier = Modifier.align(Alignment.End),
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

                        OutlinedTextField(
                            value = rubyResultTextValue,
                            onValueChange = {},
                            modifier = Modifier
                                .align(Alignment.Start),
                            readOnly = true,
                            label = { Text(text = rubyResultTextLabel) },
                            visualTransformation = NewlineMarkersVisualTransformation(
                                maker = newLineMakerString,
                                spanStyle = newLineMakerSpanStyle,
                            ),
                            singleLine = false,
                        )
                    }
                }
            }
        }
    }
}
