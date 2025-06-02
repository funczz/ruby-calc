package com.github.funczz.ruby_calc.core.interactor.program

import com.github.funczz.ruby_calc.core.data.provider.program.FindProgramDataProvider
import com.github.funczz.ruby_calc.core.model.program.ProgramModel
import com.github.funczz.ruby_calc.core.usecase.program.GetListProgramUseCase

class GetListProgramInteractor(

    private val provider: FindProgramDataProvider

) : GetListProgramUseCase {

    override fun invoke(inputData: GetListProgramUseCase.InputData): List<ProgramModel> {
        return provider.find(
            value = inputData.value,
            orderColumn = inputData.orderColumn,
            orderBy = inputData.orderBy,
            limit = inputData.limit,
        )
    }

}
