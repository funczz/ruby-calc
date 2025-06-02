package com.github.funczz.ruby_calc.core.model.element

import com.github.funczz.kotlin.logging.LoggerFactory
import com.github.funczz.kotlin.sam.SamModel
import com.github.funczz.ruby_calc.core.data.provider.element.RWElementDataProvider
import com.github.funczz.ruby_calc.core.interactor.element.GetDetailsElementInteractor
import com.github.funczz.ruby_calc.core.interactor.element.SaveElementInteractor
import com.github.funczz.ruby_calc.core.usecase.element.GetDetailsElementUseCase
import com.github.funczz.ruby_calc.core.usecase.element.SaveElementUseCase

@Suppress("Unused", "MemberVisibilityCanBePrivate")
class ElementStateModel(

    private val getDetailsElementUseCase: GetDetailsElementUseCase,

    private val saveElementUseCase: SaveElementUseCase,

    elementDetails: ElementDetails = ElementDetails(),

    ) : SamModel<ElementStateData> {

    var elementDetails: ElementDetails = elementDetails
        private set

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun present(data: ElementStateData) {
        when (data) {
            is ElementStateData.InitializeData -> {
                logger.info { "ElementStateData.InitializeData=%s".format(data.toString()) }
                data.elementDetails?.let { elementDetails = it }
            }

            is ElementStateData.InputData -> {
                when (val v = data.value) {
                    is GetDetailsElementUseCase.InputData -> {
                        logger.info { "GetDetailsElementUseCase.InputData=%s".format(v.toString()) }
                        elementDetails = ElementDetails(
                            elementList = getDetailsElementUseCase(inputData = v),
                        )
                    }

                    is SaveElementUseCase.InputData -> {
                        logger.info { "SaveElementUseCase.InputData=%s".format(v.toString()) }
                        saveElementUseCase(inputData = v)
                    }
                }
            }
        }
    }

    override fun toString(): String {
        return "ElementStateModel(elementDetails=%s)".format(elementDetails)
    }

    companion object {

        fun new(rwElementDataProvider: RWElementDataProvider): ElementStateModel =
            ElementStateModel(
                getDetailsElementUseCase = GetDetailsElementInteractor(provider = rwElementDataProvider),
                saveElementUseCase = SaveElementInteractor(provider = rwElementDataProvider),
            )
    }
}
