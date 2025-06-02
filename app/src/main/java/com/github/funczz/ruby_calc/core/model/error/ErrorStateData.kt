package com.github.funczz.ruby_calc.core.model.error

import com.github.funczz.ruby_calc.core.model.RubyCalcStateData

sealed interface ErrorStateData : RubyCalcStateData {

    data class InitializeData(
        val throwableList: List<Throwable> = emptyList(),
    ) : ErrorStateData

    data class ThrowableData(
        val throwable: Throwable,
    ) : ErrorStateData

    data class FunctionData(
        val function: () -> Unit,
    ) : ErrorStateData
}
