package com.github.funczz.ruby_calc.core.usecase.problem

import com.github.funczz.ruby_calc.core.usecase.UseCase

interface ProblemUseCase<I : ProblemUseCase.InputData, O : Any> : UseCase<I, O> {

    interface InputData : UseCase.InputData

}
