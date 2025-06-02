package com.github.funczz.ruby_calc.core.model.ruby

import com.github.funczz.ruby_calc.core.model.RubyCalcStateData
import com.github.funczz.ruby_calc.core.usecase.ruby.RubyUseCase

sealed interface RubyStateData : RubyCalcStateData {

    data class InitializeData(
        val rubyResult: RubyResult? = null,
    ) : RubyStateData

    data class InputData(
        val value: RubyUseCase.InputData
    ) : RubyStateData

}
