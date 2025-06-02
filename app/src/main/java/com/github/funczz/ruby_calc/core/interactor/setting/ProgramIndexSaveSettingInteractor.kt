package com.github.funczz.ruby_calc.core.interactor.setting

import com.github.funczz.ruby_calc.core.model.setting.SettingItems
import com.github.funczz.ruby_calc.core.usecase.setting.ProgramIndexSaveSettingUseCase
import com.github.funczz.ruby_calc.core.usecase.setting.SaveSettingUseCase

class ProgramIndexSaveSettingInteractor(

    private val useCase: SaveSettingUseCase

) : ProgramIndexSaveSettingUseCase {

    override fun invoke(inputData: ProgramIndexSaveSettingUseCase.InputData) {
        //value
        useCase(
            inputData = SaveSettingUseCase.InputData(
                settingName = SettingItems.PROGRAM_INDEX_VALUE.name,
                values = listOf(inputData.programIndex.value),
            )
        )
        //column
        useCase(
            inputData = SaveSettingUseCase.InputData(
                settingName = SettingItems.PROGRAM_INDEX_COLUMN.name,
                values = listOf(inputData.programIndex.orderColumn.name),
            )
        )
        //orderBy
        useCase(
            inputData = SaveSettingUseCase.InputData(
                settingName = SettingItems.PROGRAM_INDEX_ORDER_BY.name,
                values = listOf(inputData.programIndex.orderBy.name),
            )
        )
        //limit
        useCase(
            inputData = SaveSettingUseCase.InputData(
                settingName = SettingItems.PROGRAM_INDEX_LIMIT.name,
                values = listOf(inputData.programIndex.limit.toString()),
            )
        )
    }

}