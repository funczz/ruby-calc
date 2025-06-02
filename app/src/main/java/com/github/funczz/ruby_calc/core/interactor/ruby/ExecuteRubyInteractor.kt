package com.github.funczz.ruby_calc.core.interactor.ruby

import com.github.funczz.ruby_calc.core.service.RubyService
import com.github.funczz.ruby_calc.core.usecase.ruby.ExecuteRubyUseCase

class ExecuteRubyInteractor(

    private val rubyService: RubyService

) : ExecuteRubyUseCase {

    override fun invoke(inputData: ExecuteRubyUseCase.InputData): String {
        return rubyService.execute(
            code = inputData.code,
            argv = inputData.argv.toTypedArray()
        )
    }

}
