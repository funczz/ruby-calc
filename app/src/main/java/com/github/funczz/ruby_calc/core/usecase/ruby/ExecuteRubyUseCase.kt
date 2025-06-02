package com.github.funczz.ruby_calc.core.usecase.ruby

interface ExecuteRubyUseCase : RubyUseCase<ExecuteRubyUseCase.InputData> {

    data class InputData(
        val code: String,
        val argv: List<String>,
    ) : RubyUseCase.InputData
}
