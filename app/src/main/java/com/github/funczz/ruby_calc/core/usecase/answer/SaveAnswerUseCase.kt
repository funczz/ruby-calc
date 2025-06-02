package com.github.funczz.ruby_calc.core.usecase.answer

interface SaveAnswerUseCase : AnswerUseCase<SaveAnswerUseCase.InputData, Int> {

    data class InputData(
        val problemId: Long,
        val value: String,
    ) : AnswerUseCase.InputData

}
