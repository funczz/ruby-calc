package com.github.funczz.ruby_calc.core.interactor.setting

import com.github.funczz.ruby_calc.core.model.setting.SettingItems
import com.github.funczz.ruby_calc.core.usecase.setting.ProblemDetailsSaveSettingUseCase
import com.github.funczz.ruby_calc.core.usecase.setting.SaveSettingUseCase
import kotlin.jvm.optionals.getOrNull

class ProblemDetailsSaveSettingInteractor(

    private val useCase: SaveSettingUseCase

) : ProblemDetailsSaveSettingUseCase {

    override fun invoke(inputData: ProblemDetailsSaveSettingUseCase.InputData) {
        //id
        useCase(
            inputData = SaveSettingUseCase.InputData(
                settingName = SettingItems.PROBLEM_DETAILS_ID.name,
                values = listOf(inputData.problemId.getOrNull()?.toString() ?: ""),
            )
        )
    }

}
