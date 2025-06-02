package com.github.funczz.ruby_calc.core.interactor.answer

import com.github.funczz.ruby_calc.core.data.provider.answer.CRUDAnswerDataProvider
import com.github.funczz.ruby_calc.core.usecase.answer.SaveAnswerUseCase

class SaveAnswerInteractor(

    private val provider: CRUDAnswerDataProvider

) : SaveAnswerUseCase {

    override fun invoke(inputData: SaveAnswerUseCase.InputData): Int {
        return provider.create(
            problemId = inputData.problemId,
            value = inputData.value,
        )
    }

}
