package com.github.funczz.ruby_calc.android

import com.github.funczz.kotlin.logging.LoggerFactory
import com.github.funczz.kotlin.notifier.DefaultNotifierSubscription
import com.github.funczz.kotlin.notifier.Notifier
import com.github.funczz.kotlin.sam.SamStateRepresentation
import com.github.funczz.ruby_calc.android.event.ToastUiEvent
import com.github.funczz.ruby_calc.core.action.RubyCalcAction.REPRESENTATION_REGISTERED_NAME
import com.github.funczz.ruby_calc.core.model.RubyCalcStateModel
import java.util.Optional
import java.util.concurrent.Executor

object UiRepresentation : SamStateRepresentation<Pair<RubyCalcStateModel, UiState>, UiState> {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun representation(model: Pair<RubyCalcStateModel, UiState>): UiState {
        val (stateModel, uiState) = model

        val newProblemEditUiState = if (
            stateModel.problemStateModel.problemSaveResult.id.isPresent
            && uiState.problemEditUiState.id !is Long
        ) {
            uiState.problemEditUiState.copy(id = stateModel.problemStateModel.problemSaveResult.id.get())
        } else uiState.problemEditUiState

        val newProgramEditUiState = if (
            stateModel.programStateModel.programSaveResult.id.isPresent
            && uiState.programEditUiState.id !is Long
        ) {
            uiState.programEditUiState.copy(id = stateModel.programStateModel.programSaveResult.id.get())
        } else uiState.programEditUiState

        val newUiEvents = if (
            (stateModel.problemStateModel.problemSaveResult.id.isPresent
                    || stateModel.programStateModel.programSaveResult.id.isPresent)
            && uiState.uiEvents.none {
                it is ToastUiEvent
                        && !it.isDone
                        && it.id == R.string.save_success_message
            }
        ) {
            val newUiEvents = uiState.uiEvents.toMutableList()
            newUiEvents.add(ToastUiEvent(id = R.string.save_success_message))
            newUiEvents
        } else uiState.uiEvents

        val newUiState = uiState.copy(
            answerDetails = stateModel.answerStateModel.answerDetails,
            answerIndex = stateModel.answerStateModel.answerIndex,
            elementDetails = stateModel.elementStateModel.elementDetails,
            problemDetails = stateModel.problemStateModel.problemDetails,
            problemIndex = stateModel.problemStateModel.problemIndex,
            problemSaveResult = stateModel.problemStateModel.problemSaveResult,
            programDetails = stateModel.programStateModel.programDetails,
            programIndex = stateModel.programStateModel.programIndex,
            programSaveResult = stateModel.programStateModel.programSaveResult,
            programSelection = stateModel.programStateModel.programSelection,
            programEdit = stateModel.programStateModel.programEdit,
            rubyResult = stateModel.rubyStateModel.rubyResult,
            throwableList = stateModel.errorStateModel.throwableList.reversed(),
            problemEditUiState = newProblemEditUiState,
            programEditUiState = newProgramEditUiState,
            uiEvents = newUiEvents
        )
        logger.info { "New UiState=%s".format(newUiState.toString()) }

        return newUiState
    }

    fun subscribe(presenter: Presenter<UiState>, notifier: Notifier, executor: Executor?) {
        notifier.subscribe(
            subscription = DefaultNotifierSubscription(
                subscriber = UiRepresentationSubscriber(presenter = presenter),
                name = REPRESENTATION_REGISTERED_NAME,
                executor = Optional.ofNullable(executor)
            ),
            executor = executor
        )
    }
}
