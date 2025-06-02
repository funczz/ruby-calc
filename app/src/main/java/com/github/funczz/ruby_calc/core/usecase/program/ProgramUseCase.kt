package com.github.funczz.ruby_calc.core.usecase.program

import com.github.funczz.ruby_calc.core.usecase.UseCase

interface ProgramUseCase<I : ProgramUseCase.InputData, O : Any> : UseCase<I, O> {

    interface InputData : UseCase.InputData
}
