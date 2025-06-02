package com.github.funczz.ruby_calc.core.model.element

import com.github.funczz.ruby_calc.core.model.RubyCalcStateData
import com.github.funczz.ruby_calc.core.usecase.element.ElementUseCase

sealed interface ElementStateData : RubyCalcStateData {

    data class InitializeData(
        val elementDetails: ElementDetails? = null,
    ) : ElementStateData

    data class InputData(
        val value: ElementUseCase.InputData
    ) : ElementStateData

}
