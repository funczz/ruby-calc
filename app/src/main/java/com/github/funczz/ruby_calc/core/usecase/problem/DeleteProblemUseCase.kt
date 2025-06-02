package com.github.funczz.ruby_calc.core.usecase.problem

interface DeleteProblemUseCase : ProblemUseCase<DeleteProblemUseCase.InputData, Int> {

    data class InputData(
        val id: Long,
    ) : ProblemUseCase.InputData

}
