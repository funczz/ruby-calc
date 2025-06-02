package com.github.funczz.ruby_calc.core.usecase.program

interface DeleteProgramUseCase : ProgramUseCase<DeleteProgramUseCase.InputData, Int> {

    data class InputData(
        val id: Long,
    ) : ProgramUseCase.InputData

}
