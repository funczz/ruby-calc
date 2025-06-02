package com.github.funczz.ruby_calc.android.destination.problem

import androidx.navigation.NavHostController
import com.github.funczz.kotlin.logging.LoggerFactory
import com.github.funczz.kotlin.notifier.Notifier
import com.github.funczz.ruby_calc.android.ArgvUiState
import com.github.funczz.ruby_calc.android.Presenter
import com.github.funczz.ruby_calc.android.TextFieldState
import com.github.funczz.ruby_calc.android.UiCommand
import com.github.funczz.ruby_calc.android.UiState
import com.github.funczz.ruby_calc.core.action.accept
import com.github.funczz.ruby_calc.core.model.RubyCalcStateData
import com.github.funczz.ruby_calc.core.model.problem.ProblemDetails
import com.github.funczz.ruby_calc.core.model.problem.ProblemSaveResult
import com.github.funczz.ruby_calc.core.model.problem.ProblemStateData
import com.github.funczz.ruby_calc.core.model.ruby.RubyResult
import com.github.funczz.ruby_calc.core.model.ruby.RubyStateData
import com.github.funczz.ruby_calc.core.usecase.problem.DeleteProblemUseCase
import com.github.funczz.ruby_calc.core.usecase.problem.GetDetailsProblemUseCase
import com.github.funczz.ruby_calc.core.usecase.problem.GetListProblemUseCase
import com.github.funczz.ruby_calc.core.usecase.problem.SaveProblemUseCase
import com.github.funczz.ruby_calc.core.vo.Column
import com.github.funczz.ruby_calc.core.vo.OrderBy
import kotlin.jvm.optionals.getOrNull

object ProblemUiCommand : UiCommand {

    override val logger = LoggerFactory.getLogger(this::class.java)

    fun index(
        value: String,
        orderColumn: Column,
        orderBy: OrderBy,
        limit: Int,
        notifier: Notifier
    ) {
        val inputData = ProblemStateData.InputData(
            value = GetListProblemUseCase.InputData(
                value = value,
                orderColumn = orderColumn,
                orderBy = orderBy,
                limit = limit,
            )
        )
        logger.info { "call `notifier.accept`. GetListProblemUseCase.InputData: %s".format(inputData.toString()) }
        notifier.accept(input = inputData)
    }

    fun clearSaveResult(presenter: Presenter<UiState>, notifier: Notifier) {
        val uiState = presenter.getStateFlow().value
        if (!uiState.problemSaveResult.id.isPresent) return
        val inputData = ProblemStateData.InitializeData(
            problemSaveResult = ProblemSaveResult(),
        )
        logger.info { "call `notifier.accept`. ProblemStateData.InitializeData=%s".format(inputData.toString()) }
        notifier.accept(input = inputData)
    }

    fun close(notifier: Notifier, navHostController: NavHostController) {
        val inputData = ProblemStateData.InitializeData(
            problemSaveResult = ProblemSaveResult(),
        )
        logger.info { "call `notifier.accept`. GetListProblemUseCase.InputData: %s".format(inputData.toString()) }
        notifier.accept(input = inputData)
        navHostController.navigateUp()
    }

    fun result(id: Long, navHostController: NavHostController) {
        navHostController.navigate("%s/%d".format(ProblemDestinations.RESULT.name, id))
    }

    fun delete(id: Long, notifier: Notifier, navHostController: NavHostController) {
        navHostController.navigateUp()
        val inputData = ProblemStateData.InputData(
            value = DeleteProblemUseCase.InputData(id = id)
        )
        logger.info { "call `notifier.accept`. DeleteProblemUseCase.InputData: %s".format(inputData.toString()) }
        notifier.accept(input = inputData)
    }

    fun show(id: Long, notifier: Notifier, navHostController: NavHostController) {
        val idInputData = ProblemStateData.InputData(
            value = GetDetailsProblemUseCase.IdInputData(id = id)
        )
        logger.info {
            "call `notifier.accept`. GetDetailsProblemUseCase.InputData: %s".format(
                idInputData.toString()
            )
        }
        notifier.accept(input = idInputData)
        show(navHostController)
    }

    fun show(navHostController: NavHostController) {
        navHostController.navigate(ProblemDestinations.SHOW.name)
    }

    fun create(
        presenter: Presenter<UiState>,
        notifier: Notifier,
        navHostController: NavHostController
    ) {
        val uiState = presenter.getStateFlow().value
        val bulk = RubyCalcStateData.Bulk()
        bulk.addStateData {
            ProblemStateData.InitializeData(
                problemDetails = ProblemDetails(),
                problemSaveResult = ProblemSaveResult(),
            )
        }
        bulk.addStateData {
            RubyStateData.InitializeData(rubyResult = RubyResult())
        }
        logger.info {
            "call `notifier.accept`. RubyCalcStateData: %s".format(
                bulk.toString()
            )
        }
        notifier.accept(input = bulk)
        val newUiState = uiState.copy(
            problemEditUiState = ProblemEditUiState(),
            argvUiState = ArgvUiState(),
        )
        logger.info { "call `presenter.render`. UiState=%s".format(newUiState.toString()) }
        presenter.render(output = newUiState)
        navHostController.navigate(ProblemDestinations.CREATE.name)
    }

    fun edit(
        id: Long,
        presenter: Presenter<UiState>,
        notifier: Notifier,
        navHostController: NavHostController
    ) {
        val uiState = { presenter.getStateFlow().value }
        val bulk = RubyCalcStateData.Bulk()
        bulk.addStateData {
            ProblemStateData.InputData(
                value = GetDetailsProblemUseCase.IdInputData(id = id)
            )
        }
        bulk.addStateData {
            ProblemStateData.InitializeData(
                problemSaveResult = ProblemSaveResult()
            )
        }
        bulk.addStateData {
            RubyStateData.InitializeData(rubyResult = RubyResult())
        }
        logger.info {
            "call `notifier.accept`. RubyCalcStateData: %s".format(
                bulk.toString()
            )
        }
        notifier.accept(input = bulk)
        val problemEditUiState = uiState().problemDetails.problemModel
            .getOrNull()?.let { problemModel ->
                ProblemEditUiState(
                    id = problemModel.id,
                    name = TextFieldState(text = problemModel.name),
                    programModel = uiState().problemDetails.programModel.getOrNull(),
                    comment = TextFieldState(text = problemModel.comment),
                    createAt = problemModel.createdAt,
                    updatedAt = problemModel.updatedAt
                )
            } ?: ProblemEditUiState()

        val newArgvUiState = ArgvUiState()
        uiState().elementDetails.elementList.forEach {
            newArgvUiState.argv.add(TextFieldState(text = it.elementValue, undoManager = null))
        }
        val newUiState = uiState().copy(
            problemEditUiState = problemEditUiState,
            argvUiState = newArgvUiState,
        )
        logger.info { "call `presenter.render`. UiState=%s".format(newUiState.toString()) }
        presenter.render(output = newUiState)
        navHostController.navigate(ProblemDestinations.EDIT.name)
    }

    fun save(
        presenter: Presenter<UiState>,
        notifier: Notifier,
    ) {
        val uiState = { presenter.getStateFlow().value }
        val problemEditUiState = uiState().problemEditUiState
        val inputData = ProblemStateData.InputData(
            value = SaveProblemUseCase.InputData(
                id = problemEditUiState.id,
                name = problemEditUiState.name.value.text,
                programId = problemEditUiState.programModel!!.id,
                comment = problemEditUiState.comment.value.text,
                createdAt = null,
                updatedAt = null,
            )
        )
        logger.info { "call `notifier.accept`. SaveProgramUseCase.InputData: %s".format(inputData.toString()) }
        notifier.accept(input = inputData)
    }
}
