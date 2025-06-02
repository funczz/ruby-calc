package com.github.funczz.ruby_calc.core.usecase

interface DatabaseUseCase<I : DatabaseUseCase.InputData> : UseCase<I, Unit> {

    interface InputData : UseCase.InputData
}
