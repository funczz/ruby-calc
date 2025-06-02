package com.github.funczz.ruby_calc.android.destination.program

import androidx.navigation.NavHostController
import com.github.funczz.kotlin.logging.LoggerFactory
import com.github.funczz.kotlin.notifier.Notifier
import com.github.funczz.ruby_calc.android.ArgvUiState
import com.github.funczz.ruby_calc.android.Presenter
import com.github.funczz.ruby_calc.android.SearchBoxUiState
import com.github.funczz.ruby_calc.android.TextFieldState
import com.github.funczz.ruby_calc.android.UiCommand
import com.github.funczz.ruby_calc.android.UiState
import com.github.funczz.ruby_calc.core.action.accept
import com.github.funczz.ruby_calc.core.model.RubyCalcStateData
import com.github.funczz.ruby_calc.core.model.program.ProgramDetails
import com.github.funczz.ruby_calc.core.model.program.ProgramSaveResult
import com.github.funczz.ruby_calc.core.model.program.ProgramStateData
import com.github.funczz.ruby_calc.core.model.ruby.RubyResult
import com.github.funczz.ruby_calc.core.model.ruby.RubyStateData
import com.github.funczz.ruby_calc.core.service.RubyService
import com.github.funczz.ruby_calc.core.usecase.program.DeleteProgramUseCase
import com.github.funczz.ruby_calc.core.usecase.program.GetDetailsProgramUseCase
import com.github.funczz.ruby_calc.core.usecase.program.GetListProgramUseCase
import com.github.funczz.ruby_calc.core.usecase.program.SaveProgramUseCase
import com.github.funczz.ruby_calc.core.vo.Column
import com.github.funczz.ruby_calc.core.vo.OrderBy
import kotlin.jvm.optionals.getOrNull

object ProgramUiCommand : UiCommand {

    override val logger = LoggerFactory.getLogger(this::class.java)

    fun loadIndexSearchBoxUiState(presenter: Presenter<UiState>) {
        val uiState = presenter.getStateFlow().value
        if (uiState.programIndexSearchBoxUiState.isLoaded) return
        presenter.render(
            output = uiState.copy(
                programIndexSearchBoxUiState = SearchBoxUiState(
                    indexValue = TextFieldState(
                        text = uiState.programIndex.value,
                        undoManager = null
                    ),
                    isLoaded = true,
                )
            )
        )
    }

    fun index(
        value: String,
        orderColumn: Column,
        orderBy: OrderBy,
        limit: Int,
        notifier: Notifier
    ) {
        val inputData = ProgramStateData.InputData(
            value = GetListProgramUseCase.InputData(
                value = value,
                orderColumn = orderColumn,
                orderBy = orderBy,
                limit = limit,
            )
        )
        logger.info { "call `notifier.accept`. GetListProgramUseCase.InputData=%s".format(inputData.toString()) }
        notifier.accept(input = inputData)
    }

    fun clearSaveResult(presenter: Presenter<UiState>, notifier: Notifier) {
        val uiState = presenter.getStateFlow().value
        if (!uiState.programSaveResult.id.isPresent) return
        val inputData = ProgramStateData.InitializeData(
            programSaveResult = ProgramSaveResult(),
        )
        logger.info { "call `notifier.accept`. ProgramStateData.InitializeData=%s".format(inputData.toString()) }
        notifier.accept(input = inputData)
    }

    fun loadEditUiState(presenter: Presenter<UiState>) {
        val uiState = { presenter.getStateFlow().value }
        if (uiState().programEditUiState.isInitialized) return
        val programEdit = uiState().programEdit
        val newProgramEditUiState = ProgramEditUiState(
            id = programEdit.id,
            name = TextFieldState(text = programEdit.name),
            description = TextFieldState(text = programEdit.description),
            hint = TextFieldState(text = programEdit.hint),
            code = TextFieldState(text = programEdit.code),
            isInitialized = true,
        )
        val newUiState = uiState().copy(programEditUiState = newProgramEditUiState)
        logger.info { "call `presenter.render`. UiState=%s".format(newUiState.toString()) }
        presenter.render(output = newUiState)
    }

    fun close(notifier: Notifier, navHostController: NavHostController) {
        val inputData = ProgramStateData.InitializeData(
            programSaveResult = ProgramSaveResult(),
        )
        logger.info { "call `notifier.accept`. ProgramStateData.InitializeData=%s".format(inputData.toString()) }
        notifier.accept(input = inputData)
        navHostController.navigateUp()
    }

    fun delete(id: Long, notifier: Notifier, navHostController: NavHostController) {
        navHostController.navigateUp()
        val inputData = ProgramStateData.InputData(value = DeleteProgramUseCase.InputData(id = id))
        logger.info { "call `notifier.accept`. DeleteProgramUseCase.InputData=%s".format(inputData.toString()) }
        notifier.accept(input = inputData)
    }

    fun show(id: Long, notifier: Notifier, navHostController: NavHostController) {
        val idInputData = ProgramStateData.InputData(
            value = GetDetailsProgramUseCase.IdInputData(id = id)
        )
        logger.info {
            "call `notifier.accept`. GetDetailsProgramUseCase.InputData: %s".format(
                idInputData.toString()
            )
        }
        notifier.accept(input = idInputData)
        show(navHostController)
    }

    fun show(navHostController: NavHostController) {
        navHostController.navigate(ProgramDestinations.SHOW.name)
    }

    fun create(
        presenter: Presenter<UiState>,
        notifier: Notifier,
        navHostController: NavHostController
    ) {
        val uiState = presenter.getStateFlow().value
        val bulk = RubyCalcStateData.Bulk()
        bulk.addStateData {
            ProgramStateData.InitializeData(
                programDetails = ProgramDetails(),
                programSaveResult = ProgramSaveResult(),
            )
        }
        bulk.addStateData {
            RubyStateData.InitializeData(rubyResult = RubyResult())
        }
        logger.info {
            "call `notifier.accept`. RubyCalcStateData=%s".format(
                bulk.toString()
            )
        }
        notifier.accept(input = bulk)
        val newUiState = uiState.copy(
            programEditUiState = ProgramEditUiState(
                code = TextFieldState(text = RubyService.REQUIRED_BIGDECIMAL_MATH_JRUBY_SCRIPT_HEADER),
                isInitialized = true,
            ),
            argvUiState = ArgvUiState(),
        )
        logger.info { "call `presenter.render`. UiState=%s".format(newUiState.toString()) }
        presenter.render(output = newUiState)
        navHostController.navigate(ProgramDestinations.CREATE.name)
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
            ProgramStateData.InputData(
                value = GetDetailsProgramUseCase.IdInputData(id = id)
            )
        }
        bulk.addStateData {
            ProgramStateData.InitializeData(
                programSaveResult = ProgramSaveResult(),
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
        val programEditUiState = uiState().programDetails.programModel
            .getOrNull()?.let { programModel ->
                ProgramEditUiState(
                    id = programModel.id,
                    name = TextFieldState(text = programModel.name),
                    description = TextFieldState(text = programModel.description),
                    hint = TextFieldState(text = programModel.hint),
                    code = TextFieldState(text = programModel.code),
                    createAt = programModel.createdAt,
                    updatedAt = programModel.updatedAt,
                    isInitialized = true,
                )
            } ?: ProgramEditUiState(
            code = TextFieldState(text = RubyService.REQUIRED_BIGDECIMAL_MATH_JRUBY_SCRIPT_HEADER),
            isInitialized = true,
        )
        val newUiState = uiState().copy(
            programEditUiState = programEditUiState,
            argvUiState = ArgvUiState(),
        )
        logger.info { "call `presenter.render`. UiState=%s".format(newUiState.toString()) }
        presenter.render(output = newUiState)
        navHostController.navigate(ProgramDestinations.EDIT.name)
    }

    fun save(
        presenter: Presenter<UiState>,
        notifier: Notifier,
    ) {
        val uiState = { presenter.getStateFlow().value }
        val programUiState = uiState().programEditUiState
        val inputData = ProgramStateData.InputData(
            value = SaveProgramUseCase.InputData(
                id = programUiState.id,
                name = programUiState.name.value.text,
                description = programUiState.description.value.text,
                hint = programUiState.hint.value.text,
                code = programUiState.code.value.text,
                createdAt = null,
                updatedAt = null,
            )
        )
        logger.info { "call `notifier.accept`. SaveProgramUseCase.InputData: %s".format(inputData.toString()) }
        notifier.accept(input = inputData)
    }

}
