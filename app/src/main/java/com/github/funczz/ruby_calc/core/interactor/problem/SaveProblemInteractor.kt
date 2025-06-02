package com.github.funczz.ruby_calc.core.interactor.problem

import com.github.funczz.ruby_calc.core.data.provider.problem.CRUDProblemDataProvider
import com.github.funczz.ruby_calc.core.usecase.problem.SaveProblemUseCase

class SaveProblemInteractor(

    private val provider: CRUDProblemDataProvider

) : SaveProblemUseCase {

    override fun invoke(inputData: SaveProblemUseCase.InputData): Int {
        return when (inputData.id) {
            is Long -> {
                val updated = provider.update(
                    id = inputData.id,
                    name = inputData.name,
                    programId = inputData.programId,
                    comment = inputData.comment,
                )
                if (updated == 0) {
                    provider.create(
                        id = inputData.id,
                        name = inputData.name,
                        programId = inputData.programId,
                        comment = inputData.comment,
                        createdAt = inputData.createdAt,
                        updatedAt = inputData.updatedAt,
                    )
                } else updated
            }

            else -> {
                provider.create(
                    name = inputData.name,
                    programId = inputData.programId,
                    comment = inputData.comment,
                )
            }
        }
    }

}
