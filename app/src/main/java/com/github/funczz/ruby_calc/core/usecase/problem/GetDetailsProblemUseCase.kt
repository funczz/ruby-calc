package com.github.funczz.ruby_calc.core.usecase.problem

import com.github.funczz.ruby_calc.core.model.problem.ProblemModel
import java.util.Optional

interface GetDetailsProblemUseCase :
    ProblemUseCase<GetDetailsProblemUseCase.InputData, Optional<ProblemModel>> {

    sealed interface InputData : ProblemUseCase.InputData

    data class IdInputData(val id: Long) : InputData

    data class NameInputData(val name: String) : InputData

}
