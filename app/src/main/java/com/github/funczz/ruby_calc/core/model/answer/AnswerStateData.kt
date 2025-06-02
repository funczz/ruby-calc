package com.github.funczz.ruby_calc.core.model.answer

import com.github.funczz.ruby_calc.core.model.RubyCalcStateData
import com.github.funczz.ruby_calc.core.usecase.answer.AnswerUseCase

sealed interface AnswerStateData : RubyCalcStateData {

    data class InitializeData(
        val answerDetails: AnswerDetails? = null,
        val answerIndex: AnswerIndex? = null,
    ) : AnswerStateData

    data class InputData(
        val value: AnswerUseCase.InputData
    ) : AnswerStateData
}
