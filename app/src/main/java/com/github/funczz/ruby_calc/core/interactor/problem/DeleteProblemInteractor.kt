package com.github.funczz.ruby_calc.core.interactor.problem

import com.github.funczz.ruby_calc.core.data.provider.problem.CRUDProblemDataProvider
import com.github.funczz.ruby_calc.core.usecase.problem.DeleteProblemUseCase

class DeleteProblemInteractor(

    private val provider: CRUDProblemDataProvider

) : DeleteProblemUseCase {

    override fun invoke(inputData: DeleteProblemUseCase.InputData): Int {
        return provider.delete(inputData.id)
    }

}
