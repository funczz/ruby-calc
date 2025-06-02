package com.github.funczz.ruby_calc.core.usecase.element

interface SaveElementUseCase : ElementUseCase<SaveElementUseCase.InputData, Int> {

    data class InputData(
        val problemId: Long,
        val values: List<String>,
    ) : ElementUseCase.InputData

}
