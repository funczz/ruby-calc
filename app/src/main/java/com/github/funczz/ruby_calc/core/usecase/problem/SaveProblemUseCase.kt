package com.github.funczz.ruby_calc.core.usecase.problem

import java.time.Instant

interface SaveProblemUseCase : ProblemUseCase<SaveProblemUseCase.InputData, Int> {

    data class InputData(
        val id: Long?,
        val name: String,
        val programId: Long,
        val comment: String,
        val createdAt: Instant?,
        val updatedAt: Instant?,
    ) : ProblemUseCase.InputData

}
