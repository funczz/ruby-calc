package com.github.funczz.ruby_calc.core.usecase.program

import java.time.Instant

interface SaveProgramUseCase : ProgramUseCase<SaveProgramUseCase.InputData, Int> {

    data class InputData(
        val id: Long?,
        val name: String,
        val description: String,
        val hint: String,
        val code: String,
        val createdAt: Instant?,
        val updatedAt: Instant?,
    ) : ProgramUseCase.InputData

}
