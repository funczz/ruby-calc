package com.github.funczz.ruby_calc.core.interactor.program

import com.github.funczz.ruby_calc.core.data.provider.program.CRUDProgramDataProvider
import com.github.funczz.ruby_calc.core.usecase.program.DeleteProgramUseCase

class DeleteProgramInteractor(

    private val provider: CRUDProgramDataProvider
) : DeleteProgramUseCase {

    override fun invoke(inputData: DeleteProgramUseCase.InputData): Int {
        return provider.delete(inputData.id)
    }

}
