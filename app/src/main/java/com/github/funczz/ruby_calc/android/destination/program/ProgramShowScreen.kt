package com.github.funczz.ruby_calc.android.destination.program

import android.widget.Toast
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
import androidx.compose.material.icons.filled.Edit
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
import com.github.funczz.ruby_calc.android.Presenter
import com.github.funczz.ruby_calc.android.R
import com.github.funczz.ruby_calc.android.UiState
import com.github.funczz.ruby_calc.android.event.DeleteDialogUiEvent
import com.github.funczz.ruby_calc.android.event.ToastUiEvent
import kotlin.jvm.optionals.getOrNull

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgramShowScreen(
    presenter: Presenter<UiState>,
    notifier: Notifier,
    navHostController: NavHostController,
    modifier: Modifier,
) {
    val uiState by presenter.getState()
    val context = LocalContext.current
    //val owner = LocalLifecycleOwner.current
    val programModel = uiState.programDetails.programModel.getOrNull()

    val onClose: () -> Unit = {
        ProgramUiCommand.close(notifier = notifier, navHostController = navHostController)
    }

    val onEdit: () -> Unit = {
        uiState.programDetails.programModel.getOrNull()?.let {
            ProgramUiCommand.edit(
                id = it.id,
                presenter = presenter,
                notifier = notifier,
                navHostController = navHostController
            )
        }
    }

    val onDelete: () -> Unit = {
        when {
            uiState.programDetails.canBeDeleted -> {
                uiState.programDetails.programModel.getOrNull()?.let {
                    val deleteDialogUiEvent = DeleteDialogUiEvent(
                        confirmAction = {
                            ProgramUiCommand.delete(
                                id = it.id,
                                notifier = notifier,
                                navHostController = navHostController
                            )
                            ProgramUiCommand.delUiEvent(presenter = presenter)
                        },
                        dismissAction = {
                            ProgramUiCommand.delUiEvent(presenter = presenter)
                        }
                    )
                    ProgramUiCommand.addUiEvent(
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
                ProgramUiCommand.addUiEvent(uiEvent = toastUiEvent, presenter = presenter)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.program_details_label)) },
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
                        enabled = uiState.programDetails.canBeDeleted,
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
                value = programModel?.name ?: "",
                onValueChange = {},
                modifier = Modifier
                    .padding(20.dp)
                    .align(Alignment.Start),
                readOnly = true,
                label = { Text(text = stringResource(id = R.string.program_name_label)) },
            )
            OutlinedTextField(
                value = programModel?.description ?: "",
                onValueChange = {},
                modifier = Modifier
                    .padding(20.dp)
                    .align(Alignment.Start),
                readOnly = true,
                singleLine = false,
                label = { Text(text = stringResource(id = R.string.program_description_label)) },
            )
            OutlinedTextField(
                value = programModel?.hint ?: "",
                onValueChange = {},
                modifier = Modifier
                    .padding(20.dp)
                    .align(Alignment.Start),
                readOnly = true,
                singleLine = false,
                label = { Text(text = stringResource(id = R.string.program_hint_label)) },
            )
            OutlinedTextField(
                value = programModel?.code ?: "",
                onValueChange = {},
                modifier = Modifier
                    .padding(20.dp)
                    .align(Alignment.Start),
                readOnly = true,
                singleLine = false,
                label = { Text(text = stringResource(id = R.string.program_code_label)) },
            )
        }
    }

    ProgramUiCommand.ConsumeUiEvent(presenter = presenter, context = LocalContext.current)
}
