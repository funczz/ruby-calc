package com.github.funczz.ruby_calc.core.usecase.answer

import com.github.funczz.ruby_calc.core.usecase.UseCase

interface AnswerUseCase<I : AnswerUseCase.InputData, O : Any> : UseCase<I, O> {

    interface InputData : UseCase.InputData

}
