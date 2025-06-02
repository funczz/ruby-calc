package com.github.funczz.ruby_calc.core.usecase.ruby

import com.github.funczz.ruby_calc.core.usecase.UseCase

interface RubyUseCase<I : RubyUseCase.InputData> : UseCase<I, String> {

    interface InputData : UseCase.InputData

}
