package com.github.funczz.ruby_calc.core.usecase.program

import com.github.funczz.ruby_calc.core.model.program.ProgramModel
import java.util.Optional

interface GetDetailsProgramUseCase :
    ProgramUseCase<GetDetailsProgramUseCase.InputData, Optional<ProgramModel>> {

    sealed interface InputData : ProgramUseCase.InputData

    data class IdInputData(val id: Long) : InputData

    data class NameInputData(val name: String) : InputData

}
