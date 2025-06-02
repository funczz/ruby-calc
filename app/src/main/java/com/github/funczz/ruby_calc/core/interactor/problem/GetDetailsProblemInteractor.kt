package com.github.funczz.ruby_calc.core.interactor.problem

import com.github.funczz.ruby_calc.core.data.provider.problem.CRUDProblemDataProvider
import com.github.funczz.ruby_calc.core.model.problem.ProblemModel
import com.github.funczz.ruby_calc.core.usecase.problem.GetDetailsProblemUseCase
import java.util.Optional

class GetDetailsProblemInteractor(

    private val provider: CRUDProblemDataProvider

) : GetDetailsProblemUseCase {

    override fun invoke(inputData: GetDetailsProblemUseCase.InputData): Optional<ProblemModel> {
        return when (inputData) {
            is GetDetailsProblemUseCase.IdInputData -> provider.read(inputData.id)
            is GetDetailsProblemUseCase.NameInputData -> provider.read(name = inputData.name)
        }
    }

}
