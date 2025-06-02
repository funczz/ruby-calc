package com.github.funczz.ruby_calc.core.usecase.element

import com.github.funczz.ruby_calc.core.model.element.ElementModel

interface GetDetailsElementUseCase :
    ElementUseCase<GetDetailsElementUseCase.InputData, List<ElementModel>> {

    data class InputData(
        val problemId: Long
    ) : ElementUseCase.InputData

}
