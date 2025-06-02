package com.github.funczz.ruby_calc.core.interactor.answer

import com.github.funczz.ruby_calc.core.data.provider.answer.FindAnswerDataProvider
import com.github.funczz.ruby_calc.core.model.answer.AnswerModel
import com.github.funczz.ruby_calc.core.usecase.answer.GetListAnswerUseCase

class GetListAnswerInteractor(

    private val provider: FindAnswerDataProvider

) : GetListAnswerUseCase {

    override fun invoke(inputData: GetListAnswerUseCase.InputData): List<AnswerModel> {
        return provider.find(
            problemId = inputData.problemId,
            value = inputData.value,
            orderBy = inputData.orderBy,
            limit = inputData.limit,
        )
    }

}
