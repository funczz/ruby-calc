package com.github.funczz.ruby_calc.core.interactor.answer

import com.github.funczz.ruby_calc.core.data.provider.answer.CRUDAnswerDataProvider
import com.github.funczz.ruby_calc.core.usecase.answer.DeleteAnswerUseCase

class DeleteAnswerInteractor(

    private val provider: CRUDAnswerDataProvider

) : DeleteAnswerUseCase {

    override fun invoke(inputData: DeleteAnswerUseCase.InputData): Int {
        return provider.delete(inputData.id)
    }

}
