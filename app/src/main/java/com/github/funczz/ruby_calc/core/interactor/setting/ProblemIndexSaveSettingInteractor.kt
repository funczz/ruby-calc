package com.github.funczz.ruby_calc.core.interactor.setting

import com.github.funczz.ruby_calc.core.model.setting.SettingItems
import com.github.funczz.ruby_calc.core.usecase.setting.ProblemIndexSaveSettingUseCase
import com.github.funczz.ruby_calc.core.usecase.setting.SaveSettingUseCase

class ProblemIndexSaveSettingInteractor(

    private val useCase: SaveSettingUseCase

) : ProblemIndexSaveSettingUseCase {

    override fun invoke(inputData: ProblemIndexSaveSettingUseCase.InputData) {
        //value
        useCase(
            inputData = SaveSettingUseCase.InputData(
                settingName = SettingItems.PROBLEM_INDEX_VALUE.name,
                values = listOf(inputData.problemIndex.value),
            )
        )
        //column
        useCase(
            inputData = SaveSettingUseCase.InputData(
                settingName = SettingItems.PROBLEM_INDEX_COLUMN.name,
                values = listOf(inputData.problemIndex.orderColumn.name),
            )
        )
        //orderBy
        useCase(
            inputData = SaveSettingUseCase.InputData(
                settingName = SettingItems.PROBLEM_INDEX_ORDER_BY.name,
                values = listOf(inputData.problemIndex.orderBy.name),
            )
        )
        //limit
        useCase(
            inputData = SaveSettingUseCase.InputData(
                settingName = SettingItems.PROBLEM_INDEX_LIMIT.name,
                values = listOf(inputData.problemIndex.limit.toString()),
            )
        )
    }

}