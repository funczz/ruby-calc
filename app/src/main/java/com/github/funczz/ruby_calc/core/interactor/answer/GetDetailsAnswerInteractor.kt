package com.github.funczz.ruby_calc.core.interactor.answer

import com.github.funczz.ruby_calc.core.data.provider.answer.CRUDAnswerDataProvider
import com.github.funczz.ruby_calc.core.model.answer.AnswerModel
import com.github.funczz.ruby_calc.core.usecase.answer.GetDetailsAnswerUseCase
import java.util.Optional

class GetDetailsAnswerInteractor(

    private val provider: CRUDAnswerDataProvider

) : GetDetailsAnswerUseCase {

    override fun invoke(inputData: GetDetailsAnswerUseCase.InputData): Optional<AnswerModel> {
        return provider.read(inputData.id)
    }

}
