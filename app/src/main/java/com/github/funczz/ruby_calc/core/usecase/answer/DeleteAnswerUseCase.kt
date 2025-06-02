package com.github.funczz.ruby_calc.core.usecase.answer

interface DeleteAnswerUseCase : AnswerUseCase<DeleteAnswerUseCase.InputData, Int> {

    data class InputData(
        val id: Long,
    ) : AnswerUseCase.InputData

}
