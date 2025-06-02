package com.github.funczz.ruby_calc.core.usecase

interface UseCase<I : UseCase.InputData, O : Any> {

    operator fun invoke(inputData: I): O

    interface InputData

}
