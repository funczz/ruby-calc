package com.github.funczz.ruby_calc.core.interactor.setting

import com.github.funczz.ruby_calc.core.model.setting.SettingItems
import com.github.funczz.ruby_calc.core.usecase.setting.ProgramDetailsSaveSettingUseCase
import com.github.funczz.ruby_calc.core.usecase.setting.SaveSettingUseCase
import kotlin.jvm.optionals.getOrNull

class ProgramDetailsSaveSettingInteractor(

    private val useCase: SaveSettingUseCase

) : ProgramDetailsSaveSettingUseCase {

    override fun invoke(inputData: ProgramDetailsSaveSettingUseCase.InputData) {
        //id
        useCase(
            inputData = SaveSettingUseCase.InputData(
                settingName = SettingItems.PROGRAM_DETAILS_ID.name,
                values = listOf(inputData.programId.getOrNull()?.toString() ?: ""),
            )
        )
    }

}
