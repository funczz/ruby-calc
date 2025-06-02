package com.github.funczz.ruby_calc.core.interactor.element

import com.github.funczz.ruby_calc.core.data.provider.element.RWElementDataProvider
import com.github.funczz.ruby_calc.core.model.element.ElementModel
import com.github.funczz.ruby_calc.core.usecase.element.GetDetailsElementUseCase

class GetDetailsElementInteractor(

    private val provider: RWElementDataProvider

) : GetDetailsElementUseCase {

    override fun invoke(inputData: GetDetailsElementUseCase.InputData): List<ElementModel> {
        return provider.read(inputData.problemId)
    }

}
