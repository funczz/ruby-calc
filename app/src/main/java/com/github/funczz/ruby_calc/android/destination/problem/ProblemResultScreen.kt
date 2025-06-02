package com.github.funczz.ruby_calc.android.destination.problem

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.github.funczz.kotlin.notifier.Notifier
import com.github.funczz.ruby_calc.android.NewlineMarkersVisualTransformation
import com.github.funczz.ruby_calc.android.Presenter
import com.github.funczz.ruby_calc.android.R
import com.github.funczz.ruby_calc.android.UiState
import com.github.funczz.ruby_calc.android.event.DeleteDialogUiEvent
import com.github.funczz.ruby_calc.android.newlineMarker
import com.github.funczz.ruby_calc.core.action.accept
import com.github.funczz.ruby_calc.core.model.answer.AnswerStateData
import com.github.funczz.ruby_calc.core.usecase.answer.GetDetailsAnswerUseCase
import kotlin.jvm.optionals.getOrNull

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProblemResultScreen(
    id: Long?,
    presenter: Presenter<UiState>,
    notifier: Notifier,
    navHostController: NavHostController,
    modifier: Modifier,
) {
    val uiState by presenter.getState()
    //val context = LocalContext.current
    //val owner = LocalLifecycleOwner.current
    val answerModel = uiState.answerDetails.answerModel.getOrNull()
    val created = uiState.answerDetails.answerModel.getOrNull()?.let {
        ProblemUiCommand.dateFormat(
            instant = it.createdAt,
            pattern = stringResource(id = R.string.datetime_formatter_pattern)
        )
    } ?: ""
    if (id is Long) {
        val inputData = AnswerStateData.InputData(
            value = GetDetailsAnswerUseCase.InputData(
                id = id,
            )
        )
        notifier.accept(input = inputData)
    }

    val onClose: () -> Unit = {
        ProblemUiCommand.close(notifier = notifier, navHostController = navHostController)
    }

    val onDelete: () -> Unit = {
        uiState.answerDetails.answerModel.getOrNull()?.let {
            val deleteDialogUiEvent = DeleteDialogUiEvent(
                confirmAction = {
                    ProblemUiCommand.deleteResult(
                        id = it.id,
                        notifier = notifier,
                        navHostController = navHostController
                    )
                    ProblemUiCommand.delUiEvent(presenter = presenter)
                },
                dismissAction = {
                    ProblemUiCommand.delUiEvent(presenter = presenter)
                }
            )
            ProblemUiCommand.addUiEvent(
                uiEvent = deleteDialogUiEvent,
                presenter = presenter
            )
        }

    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = created)
                },
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack, //Icons.Default.ArrowBack,
                            contentDescription = stringResource(id = R.string.back_content_description)
                        )
                    }
                },
                actions = {
                    Button(
                        onClick = onDelete,
                        enabled = true,
                        colors = ButtonDefaults.buttonColors(
                            contentColor = colorResource(id = R.color.bootstrap_light),
                            containerColor = colorResource(id = R.color.bootstrap_danger),
                            disabledContentColor = colorResource(id = R.color.bootstrap_light),
                            disabledContainerColor = colorResource(id = R.color.bootstrap_secondary),
                        )
                    ) {
                        Icon(
                            Icons.Filled.DeleteOutline,
                            contentDescription = stringResource(id = R.string.delete_content_description),
                            modifier = Modifier.size(ButtonDefaults.IconSize)
                        )
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text(stringResource(id = R.string.delete_content_description))
                    }
                }
            )
        }
    ) {

        Column(
            modifier = modifier
                .padding(it)
                .fillMaxSize()
                .horizontalScroll(rememberScrollState())
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            OutlinedTextField(
                value = answerModel
                    ?.value
                    ?.newlineMarker(NewlineMarkersVisualTransformation.NEWLINE_MAKER_STRING)
                    ?: "",
                onValueChange = {},
                modifier = Modifier
                    .padding(20.dp)
                    .align(Alignment.Start),
                readOnly = true,
                label = { Text(text = stringResource(id = R.string.calculator_success_result_label)) },
                visualTransformation = NewlineMarkersVisualTransformation(
                    maker = NewlineMarkersVisualTransformation.NEWLINE_MAKER_STRING,
                    spanStyle = NewlineMarkersVisualTransformation.NEWLINE_MAKER_SPAN_STYLE,
                ),
                singleLine = false,
                minLines = 1,
            )
        }
    }

    ProblemUiCommand.ConsumeUiEvent(presenter = presenter, context = LocalContext.current)
}
