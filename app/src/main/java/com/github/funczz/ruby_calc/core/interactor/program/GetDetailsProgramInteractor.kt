package com.github.funczz.ruby_calc.core.interactor.program

import com.github.funczz.ruby_calc.core.data.provider.program.CRUDProgramDataProvider
import com.github.funczz.ruby_calc.core.model.program.ProgramModel
import com.github.funczz.ruby_calc.core.usecase.program.GetDetailsProgramUseCase
import java.util.Optional

class GetDetailsProgramInteractor(

    private val provider: CRUDProgramDataProvider

) : GetDetailsProgramUseCase {

    override fun invoke(inputData: GetDetailsProgramUseCase.InputData): Optional<ProgramModel> {
        return when (inputData) {
            is GetDetailsProgramUseCase.IdInputData -> provider.read(inputData.id)
            is GetDetailsProgramUseCase.NameInputData -> provider.read(name = inputData.name)
        }
    }

}
