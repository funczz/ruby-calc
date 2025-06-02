package com.github.funczz.ruby_calc.android.destination.system

import androidx.navigation.NavHostController
import com.github.funczz.kotlin.logging.LoggerFactory
import com.github.funczz.kotlin.notifier.Notifier
import com.github.funczz.ruby_calc.android.Presenter
import com.github.funczz.ruby_calc.android.R
import com.github.funczz.ruby_calc.android.UiCommand
import com.github.funczz.ruby_calc.android.UiState
import com.github.funczz.ruby_calc.android.event.ProgressUiEvent
import com.github.funczz.ruby_calc.android.event.ToastUiEvent
import com.github.funczz.ruby_calc.core.action.accept
import com.github.funczz.ruby_calc.core.model.error.ErrorStateData
import com.github.funczz.ruby_calc.core.service.BackupService
import com.github.funczz.ruby_calc.instance.ExecutorServiceProvider
import java.io.InputStream
import java.io.OutputStream
import java.util.Optional
import java.util.concurrent.Executor

object SystemUiCommand : UiCommand {

    override val logger = LoggerFactory.getLogger(this::class.java)

    private val executor: Executor
        get() = ExecutorServiceProvider.getInstance()

    fun close(navHostController: NavHostController) {
        navHostController.navigateUp()
    }

    fun show(index: Int, presenter: Presenter<UiState>, navHostController: NavHostController) {
        val uiState = presenter.getStateFlow().value
        presenter.render(
            output = uiState.copy(
                throwableUiState = Optional.ofNullable(uiState.throwableList[index])
            )
        )
        navHostController.navigate(SystemDestinations.SHOW.name)
    }

    fun backup(
        outputStream: OutputStream?,
        backupService: BackupService,
        executor: Executor?,
        presenter: Presenter<UiState>,
        notifier: Notifier,
    ) {
        if (outputStream == null) {
            SystemUiCommand.addUiEvent(
                uiEvent = ToastUiEvent(id = R.string.open_file_failure_message),
                presenter = presenter
            )
            return
        }
        val progressUiEvent = ProgressUiEvent(
            executor = executor ?: this.executor,
            backgroundAction = {
                outputStream.use {
                    backupService.backup(backup = it)
                    SystemUiCommand.addUiEvent(
                        uiEvent = ToastUiEvent(id = R.string.backup_success_message),
                        presenter = presenter
                    )
                }
            },
            throwAction = {
                logger.severe { it.stackTraceToString() }
                notifier.accept(input = ErrorStateData.ThrowableData(it))
            },
            finallyAction = {
                SystemUiCommand.delUiEvent(presenter = presenter)
            }
        )
        SystemUiCommand.addUiEvent(uiEvent = progressUiEvent, presenter = presenter)
    }

    fun restore(
        inputStream: InputStream?,
        backupService: BackupService,
        executor: Executor?,
        presenter: Presenter<UiState>,
        notifier: Notifier,
    ) {
        if (inputStream == null) {
            SystemUiCommand.addUiEvent(
                uiEvent = ToastUiEvent(id = R.string.open_file_failure_message),
                presenter = presenter
            )
            return
        }
        val progressUiEvent = ProgressUiEvent(
            executor = executor ?: this.executor,
            backgroundAction = {
                inputStream.use {
                    backupService.restore(backup = it)
                    SystemUiCommand.addUiEvent(
                        uiEvent = ToastUiEvent(id = R.string.restore_success_message),
                        presenter = presenter
                    )
                }
            },
            throwAction = {
                logger.severe { it.stackTraceToString() }
                notifier.accept(input = ErrorStateData.ThrowableData(it))
            },
            finallyAction = {
                SystemUiCommand.delUiEvent(presenter = presenter)
            }
        )
        SystemUiCommand.addUiEvent(uiEvent = progressUiEvent, presenter = presenter)
    }

}
