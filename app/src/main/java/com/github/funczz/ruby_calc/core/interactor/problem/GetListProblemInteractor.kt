package com.github.funczz.ruby_calc.core.interactor.problem

import com.github.funczz.ruby_calc.core.data.provider.problem.FindProblemDataProvider
import com.github.funczz.ruby_calc.core.model.problem.ProblemModel
import com.github.funczz.ruby_calc.core.usecase.problem.GetListProblemUseCase

class GetListProblemInteractor(

    private val provider: FindProblemDataProvider

) : GetListProblemUseCase {

    override fun invoke(inputData: GetListProblemUseCase.InputData): List<ProblemModel> {
        return provider.find(
            programId = inputData.programId,
            value = inputData.value,
            orderColumn = inputData.orderColumn,
            orderBy = inputData.orderBy,
            limit = inputData.limit,
        )
    }

}
