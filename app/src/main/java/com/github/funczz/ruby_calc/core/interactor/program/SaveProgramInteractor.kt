package com.github.funczz.ruby_calc.core.interactor.program

import com.github.funczz.ruby_calc.core.data.provider.program.CRUDProgramDataProvider
import com.github.funczz.ruby_calc.core.usecase.program.SaveProgramUseCase

class SaveProgramInteractor(

    private val provider: CRUDProgramDataProvider,

    ) : SaveProgramUseCase {

    override fun invoke(inputData: SaveProgramUseCase.InputData): Int {
        return when (inputData.id) {
            is Long -> {
                val updated = provider.update(
                    id = inputData.id,
                    name = inputData.name,
                    description = inputData.description,
                    hint = inputData.hint,
                    code = inputData.code,
                )
                if (updated == 0) {
                    provider.create(
                        id = inputData.id,
                        name = inputData.name,
                        description = inputData.description,
                        hint = inputData.hint,
                        code = inputData.code,
                        createdAt = inputData.createdAt,
                        updatedAt = inputData.updatedAt,
                    )
                } else updated
            }

            else -> {
                provider.create(
                    name = inputData.name,
                    description = inputData.description,
                    hint = inputData.hint,
                    code = inputData.code,
                )
            }
        }
    }

}
