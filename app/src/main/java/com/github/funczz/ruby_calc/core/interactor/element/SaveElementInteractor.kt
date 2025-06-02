package com.github.funczz.ruby_calc.core.interactor.element

import com.github.funczz.ruby_calc.core.data.provider.element.RWElementDataProvider
import com.github.funczz.ruby_calc.core.usecase.element.SaveElementUseCase

class SaveElementInteractor(

    private val provider: RWElementDataProvider

) : SaveElementUseCase {

    override fun invoke(inputData: SaveElementUseCase.InputData): Int {
        return provider.write(inputData.problemId, inputData.values)
    }

}
