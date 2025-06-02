package com.github.funczz.ruby_calc.core.usecase.element

import com.github.funczz.ruby_calc.core.usecase.UseCase

interface ElementUseCase<I : ElementUseCase.InputData, O : Any> : UseCase<I, O> {

    interface InputData : UseCase.InputData
}
