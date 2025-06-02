package com.github.funczz.ruby_calc.android.destination.system

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.github.funczz.kotlin.notifier.Notifier
import com.github.funczz.ruby_calc.android.Presenter
import com.github.funczz.ruby_calc.android.R
import com.github.funczz.ruby_calc.android.UiState
import com.github.funczz.ruby_calc.android.event.LicenseDialogUiEvent
import com.github.funczz.ruby_calc.android.parts.ErrorStateIndexBox
import com.github.funczz.ruby_calc.core.action.accept
import com.github.funczz.ruby_calc.core.model.error.ErrorStateData
import com.github.funczz.ruby_calc.core.service.BackupService
import com.github.funczz.ruby_calc.core.vo.FileName
import com.github.funczz.ruby_calc.core.vo.MIMEType
import java.util.concurrent.Executor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SystemIndexScreen(
    presenter: Presenter<UiState>,
    notifier: Notifier,
    backupService: BackupService,
    executor: Executor,
    navHostController: NavHostController,
    modifier: Modifier,
) {
    val uiState by presenter.getState()
    val context = LocalContext.current
    //val owner = LocalLifecycleOwner.current

    val backupLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode != Activity.RESULT_OK) {
            return@rememberLauncherForActivityResult
        }
        result.data?.data?.let { uri ->
            val inputData = ErrorStateData.FunctionData {
                val contentResolver = context.applicationContext.contentResolver
                contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )
                SystemUiCommand.backup(
                    outputStream = contentResolver.openOutputStream(uri),
                    backupService = backupService,
                    executor = executor,
                    presenter = presenter,
                    notifier = notifier,
                )
            }
            notifier.accept(input = inputData)
        }
    }

    val restoreLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode != Activity.RESULT_OK) {
            return@rememberLauncherForActivityResult
        }
        result.data?.data?.let { uri ->
            val inputData = ErrorStateData.FunctionData {
                val contentResolver = context.applicationContext.contentResolver
                contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )
                SystemUiCommand.restore(
                    inputStream = contentResolver.openInputStream(uri),
                    backupService = backupService,
                    executor = executor,
                    presenter = presenter,
                    notifier = notifier,
                )
            }
            notifier.accept(input = inputData)
        }
    }

    val onBackup: () -> Unit = {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = MIMEType.BACKUP_FILE
            putExtra(Intent.EXTRA_TITLE, FileName.defaultBackupFileName)
        }
        backupLauncher.launch(intent)
    }

    val onRestore: () -> Unit = {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = MIMEType.BACKUP_FILE
        }
        restoreLauncher.launch(intent)
    }

    val onClick: (Int) -> Unit = {
        SystemUiCommand.show(
            index = it,
            presenter = presenter,
            navHostController = navHostController
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    SystemDropdownMenuBox(
                        assetsDirectory = "license",
                        presenter = presenter,
                        modifier = modifier,
                    )
                },
                actions = {
                    Button(
                        onClick = onBackup,
                        colors = ButtonDefaults.buttonColors(
                            contentColor = colorResource(id = R.color.bootstrap_light),
                            containerColor = colorResource(id = R.color.bootstrap_primary),
                            disabledContentColor = colorResource(id = R.color.bootstrap_light),
                            disabledContainerColor = colorResource(id = R.color.bootstrap_secondary),
                        )
                    ) {
                        Icon(
                            Icons.Filled.Backup,
                            contentDescription = stringResource(id = R.string.backup_content_description),
                            modifier = Modifier.size(ButtonDefaults.IconSize)
                        )
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text(stringResource(id = R.string.system_backup_label))
                    }
                    Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                    Button(
                        onClick = onRestore,
                        colors = ButtonDefaults.buttonColors(
                            contentColor = colorResource(id = R.color.bootstrap_light),
                            containerColor = colorResource(id = R.color.bootstrap_danger),
                            disabledContentColor = colorResource(id = R.color.bootstrap_light),
                            disabledContainerColor = colorResource(id = R.color.bootstrap_secondary),
                        )
                    ) {
                        Icon(
                            Icons.Filled.Restore,
                            contentDescription = stringResource(id = R.string.restore_content_description),
                            modifier = Modifier.size(ButtonDefaults.IconSize)
                        )
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text(stringResource(id = R.string.system_restore_label))
                    }
                }
            )
        }
    ) {
        ErrorStateIndexBox(
            throwableList = uiState.throwableList,
            paddingValues = it,
            modifier = modifier,
            onClick = onClick,
        )
    }

    SystemUiCommand.ConsumeUiEvent(presenter = presenter, context = LocalContext.current)
}


@Composable
fun SystemDropdownMenuBox(
    assetsDirectory: String,
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
                text = { Text(text = stringResource(id = R.string.system_menu_view_license_label)) },
                onClick = {
                    onSelect {
                        SystemUiCommand.addUiEvent(
                            uiEvent = LicenseDialogUiEvent(
                                assetsDirectory = assetsDirectory,
                                confirmAction = { SystemUiCommand.delUiEvent(presenter = presenter) },
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
