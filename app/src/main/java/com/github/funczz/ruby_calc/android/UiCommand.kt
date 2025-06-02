package com.github.funczz.ruby_calc.android

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.github.funczz.kotlin.logging.Logger
import com.github.funczz.kotlin.notifier.Notifier
import com.github.funczz.ruby_calc.android.event.CloseDialogUiEvent
import com.github.funczz.ruby_calc.android.event.DeleteDialogUiEvent
import com.github.funczz.ruby_calc.android.event.LicenseDialogUiEvent
import com.github.funczz.ruby_calc.android.event.ProgramSelectionDialogUiEvent
import com.github.funczz.ruby_calc.android.event.ProgressUiEvent
import com.github.funczz.ruby_calc.android.event.SaveDialogUiEvent
import com.github.funczz.ruby_calc.android.event.SingleSelectionDialogUiEvent
import com.github.funczz.ruby_calc.android.event.TextViewerDialogUiEvent
import com.github.funczz.ruby_calc.android.event.ToastUiEvent
import com.github.funczz.ruby_calc.android.event.UiEvent
import com.github.funczz.ruby_calc.core.action.accept
import com.github.funczz.ruby_calc.core.model.answer.AnswerStateData
import com.github.funczz.ruby_calc.core.model.element.ElementStateData
import com.github.funczz.ruby_calc.core.model.program.ProgramStateData
import com.github.funczz.ruby_calc.core.model.ruby.RubyStateData
import com.github.funczz.ruby_calc.core.usecase.answer.DeleteAnswerUseCase
import com.github.funczz.ruby_calc.core.usecase.answer.SaveAnswerUseCase
import com.github.funczz.ruby_calc.core.usecase.element.SaveElementUseCase
import com.github.funczz.ruby_calc.core.usecase.ruby.ExecuteRubyUseCase
import com.github.funczz.ruby_calc.core.vo.Column
import com.github.funczz.ruby_calc.core.vo.OrderBy
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

interface UiCommand {

    val logger: Logger

    fun addUiEvent(uiEvent: UiEvent, presenter: Presenter<UiState>) {
        val uiState = presenter.getStateFlow().value
        val uiEvents = uiState.uiEvents + uiEvent
        val newUiState = uiState.copy(uiEvents = uiEvents)
        presenter.render(output = newUiState)
    }

    fun delUiEvent(presenter: Presenter<UiState>) {
        val uiState = presenter.getStateFlow().value
        val uiEvents = uiState.uiEvents
        val newUiState = uiState.copy(uiEvents = uiEvents.filter { !it.isDone })
        presenter.render(output = newUiState)
    }

    @Composable
    fun ConsumeUiEvent(presenter: Presenter<UiState>, context: Context) {
        val uiState = presenter.getState().value
        val uiEvents = uiState.uiEvents
        if (uiEvents.isEmpty()) return
        when (val event = uiEvents.first()) {
            is ProgramSelectionDialogUiEvent -> event.Show()
            is CloseDialogUiEvent -> event.Show()
            is DeleteDialogUiEvent -> event.Show()
            is SaveDialogUiEvent -> event.Show()
            is SingleSelectionDialogUiEvent<*> -> event.Show()
            is ProgressUiEvent -> event.Show()
            is LicenseDialogUiEvent -> event.Show(context = context)
            is TextViewerDialogUiEvent -> event.Show()
            is ToastUiEvent -> event.Show(context = context)
        }
        delUiEvent(presenter = presenter)
    }

    fun programSelection(
        value: String,
        orderColumn: Column,
        orderBy: OrderBy,
        limit: Int,
        notifier: Notifier
    ) {
        val inputData = ProgramStateData.SelectionData.new(
            value = value,
            orderColumn = orderColumn,
            orderBy = orderBy,
            limit = limit,
        )
        logger.info { "call `notifier.accept`. ProgramStateData.SelectionData: %s".format(inputData.toString()) }
        notifier.accept(input = inputData)
    }


    fun calculate(code: String, argv: List<String>, notifier: Notifier) {
        val inputData = RubyStateData.InputData(
            value = ExecuteRubyUseCase.InputData(
                code = code,
                argv = argv,
            )
        )
        logger.info { "call `notifier.accept`. ExecuteRubyUseCase.InputData: %s".format(inputData.toString()) }
        notifier.accept(input = inputData)
    }

    fun saveARGV(argv: List<String>, problemId: Long, notifier: Notifier) {
        val inputData = ElementStateData.InputData(
            value = SaveElementUseCase.InputData(
                problemId = problemId,
                values = argv,
            )
        )
        logger.info { "call `notifier.accept`. SaveElementUseCase.InputData: %s".format(inputData.toString()) }
        notifier.accept(input = inputData)
    }

    fun saveResult(result: String, problemId: Long, notifier: Notifier) {
        val inputData = AnswerStateData.InputData(
            value = SaveAnswerUseCase.InputData(
                problemId = problemId,
                value = result,
            )
        )
        logger.info { "call `notifier.accept`. SaveAnswerUseCase.InputData: %s".format(inputData.toString()) }
        notifier.accept(input = inputData)
    }

    fun deleteResult(id: Long, notifier: Notifier, navHostController: NavHostController) {
        val inputData = AnswerStateData.InputData(
            value = DeleteAnswerUseCase.InputData(
                id = id,
            )
        )
        logger.info { "call `notifier.accept`. DeleteAnswerUseCase.InputData: %s".format(inputData.toString()) }
        notifier.accept(input = inputData)
        navHostController.navigateUp()
    }

    fun dateFormat(instant: Instant, pattern: String): String {
        return DateTimeFormatter.ofPattern(pattern).format(
            LocalDateTime.ofInstant(instant, ZoneOffset.UTC)
        )
    }
}
