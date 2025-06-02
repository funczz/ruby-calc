package com.github.funczz.ruby_calc.android

import com.github.funczz.ruby_calc.android.destination.problem.ProblemEditUiState
import com.github.funczz.ruby_calc.android.destination.program.ProgramEditUiState
import com.github.funczz.ruby_calc.android.event.UiEvent
import com.github.funczz.ruby_calc.core.model.answer.AnswerDetails
import com.github.funczz.ruby_calc.core.model.answer.AnswerIndex
import com.github.funczz.ruby_calc.core.model.element.ElementDetails
import com.github.funczz.ruby_calc.core.model.problem.ProblemDetails
import com.github.funczz.ruby_calc.core.model.problem.ProblemIndex
import com.github.funczz.ruby_calc.core.model.problem.ProblemSaveResult
import com.github.funczz.ruby_calc.core.model.program.ProgramDetails
import com.github.funczz.ruby_calc.core.model.program.ProgramIndex
import com.github.funczz.ruby_calc.core.model.program.ProgramSaveResult
import com.github.funczz.ruby_calc.core.model.ruby.RubyResult
import java.util.Optional

data class UiState(
    //answerStateModel (RubyCalcStateModel)
    val answerDetails: AnswerDetails = AnswerDetails(),
    val answerIndex: AnswerIndex = AnswerIndex(),

    //elementStateModel (RubyCalcStateModel)
    val elementDetails: ElementDetails = ElementDetails(),

    //problemStateModel (RubyCalcStateModel)
    val problemDetails: ProblemDetails = ProblemDetails(),
    val problemIndex: ProblemIndex = ProblemIndex(),
    val problemSaveResult: ProblemSaveResult = ProblemSaveResult(),

    //programStateModel (RubyCalcStateModel)
    val programDetails: ProgramDetails = ProgramDetails(),
    val programIndex: ProgramIndex = ProgramIndex(),
    val programSaveResult: ProgramSaveResult = ProgramSaveResult(),
    val programSelection: ProgramIndex = ProgramIndex(),
    val programSelectionSearchBoxUiState: SearchBoxUiState = SearchBoxUiState(),

    //rubyStateModel (RubyCalcStateModel)
    val rubyResult: RubyResult = RubyResult(),

    //errorStateModel (RubyCalcStateModel)
    val throwableList: List<Throwable> = emptyList(),

    //errorStateModel UiState
    val throwableUiState: Optional<Throwable> = Optional.empty(),

    //problemEditUiState
    val problemEditUiState: ProblemEditUiState = ProblemEditUiState(),

    //problemIndexSearchBoxUiState
    val problemIndexSearchBoxUiState: SearchBoxUiState = SearchBoxUiState(),

    //programEditUiState
    val programEditUiState: ProgramEditUiState = ProgramEditUiState(),

    //programIndexSearchBoxUiState
    val programIndexSearchBoxUiState: SearchBoxUiState = SearchBoxUiState(),

    //argvUiState
    val argvUiState: ArgvUiState = ArgvUiState(),

    //uiEvent
    val uiEvents: List<UiEvent> = emptyList(),

    )
