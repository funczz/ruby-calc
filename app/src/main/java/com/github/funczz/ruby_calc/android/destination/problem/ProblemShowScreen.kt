package com.github.funczz.ruby_calc.android.destination.problem

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.github.funczz.kotlin.notifier.Notifier
import com.github.funczz.ruby_calc.android.Presenter
import com.github.funczz.ruby_calc.android.R
import com.github.funczz.ruby_calc.android.UiState
import com.github.funczz.ruby_calc.android.event.DeleteDialogUiEvent
import com.github.funczz.ruby_calc.android.event.ToastUiEvent
import com.github.funczz.ruby_calc.android.newlineMarker
import kotlin.jvm.optionals.getOrNull

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProblemShowScreen(
    presenter: Presenter<UiState>,
    notifier: Notifier,
    navHostController: NavHostController,
    modifier: Modifier,
) {
    val uiState by presenter.getState()
    val context = LocalContext.current
    //val owner = LocalLifecycleOwner.current
    val problemModel = uiState.problemDetails.problemModel.getOrNull()
    val programModel = uiState.problemDetails.programModel.getOrNull()

    val onClose: () -> Unit = {
        ProblemUiCommand.close(notifier = notifier, navHostController = navHostController)
    }

    val onEdit: () -> Unit = {
        uiState.problemDetails.problemModel.getOrNull()?.let {
            ProblemUiCommand.edit(
                id = it.id,
                presenter = presenter,
                notifier = notifier,
                navHostController = navHostController
            )
        }
    }

    val onDelete: () -> Unit = {
        when {
            uiState.problemDetails.canBeDeleted -> {
                uiState.problemDetails.problemModel.getOrNull()?.let {
                    val deleteDialogUiEvent = DeleteDialogUiEvent(
                        confirmAction = {
                            ProblemUiCommand.delete(
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

            else -> {
                val toastUiEvent = ToastUiEvent(
                    message = context.getString(R.string.do_not_delete_warning_message),
                    duration = Toast.LENGTH_SHORT
                )
                ProblemUiCommand.addUiEvent(uiEvent = toastUiEvent, presenter = presenter)
            }
        }
    }

    val onClick: (Long) -> Unit = {
        ProblemUiCommand.result(
            id = it,
            navHostController = navHostController
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.problem_details_label)) },
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
                        onClick = onEdit,
                        colors = ButtonDefaults.buttonColors(
                            contentColor = colorResource(id = R.color.bootstrap_light),
                            containerColor = colorResource(id = R.color.bootstrap_primary),
                            disabledContentColor = colorResource(id = R.color.bootstrap_light),
                            disabledContainerColor = colorResource(id = R.color.bootstrap_secondary),
                        )
                    ) {
                        Icon(
                            Icons.Filled.Edit,
                            contentDescription = stringResource(id = R.string.edit_content_description),
                            modifier = Modifier.size(ButtonDefaults.IconSize)
                        )
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text(stringResource(id = R.string.edit_content_description))
                    }
                    Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                    Button(
                        onClick = onDelete,
                        enabled = uiState.problemDetails.canBeDeleted,
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
                .fillMaxSize(),
            //.horizontalScroll(rememberScrollState())
            //.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            OutlinedTextField(
                value = problemModel?.name ?: "",
                onValueChange = {},
                modifier = Modifier
                    .padding(20.dp)
                    .align(Alignment.Start),
                readOnly = true,
                label = { Text(text = stringResource(id = R.string.problem_name_label)) },
            )
            OutlinedTextField(
                value = programModel?.name ?: "",
                onValueChange = {},
                modifier = Modifier
                    .padding(20.dp)
                    .align(Alignment.Start),
                readOnly = true,
                singleLine = false,
                label = { Text(text = stringResource(id = R.string.problem_program_name_label)) },
            )
            OutlinedTextField(
                value = problemModel?.comment ?: "",
                onValueChange = {},
                modifier = Modifier
                    .padding(20.dp)
                    .align(Alignment.Start),
                readOnly = true,
                singleLine = false,
                label = { Text(text = stringResource(id = R.string.problem_comment_label)) },
            )

            LazyColumn {
                items(uiState.answerIndex.answerList.size) { index ->
                    val model = uiState.answerIndex.answerList[index]
                    val created = ProblemUiCommand.dateFormat(
                        instant = model.createdAt,
                        pattern = stringResource(id = R.string.datetime_formatter_pattern)
                    )
                    Surface(
                        modifier = modifier
                            .clickable { onClick(model.id) },
                        shape = MaterialTheme.shapes.medium,
                    ) {
                        Column(
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 16.dp),
                        ) {
                            Text(
                                text = model.value.newlineMarker(),
                                maxLines = 5,
                                style = MaterialTheme.typography.titleSmall,
                            )
                            Spacer(modifier = modifier.height(4.dp))
                            Text(
                                text = stringResource(
                                    id = R.string.create_at_content_format,
                                    created
                                ),
                                modifier = Modifier
                                    .fillParentMaxWidth()
                                    .padding(end = 20.dp),
                                style = MaterialTheme.typography.bodySmall,
                                textAlign = TextAlign.Right,
                            )
                            HorizontalDivider(
                                thickness = 1.dp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

        }
    }

    ProblemUiCommand.ConsumeUiEvent(presenter = presenter, context = LocalContext.current)
}
