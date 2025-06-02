package com.github.funczz.ruby_calc.core.interactor.setting

import com.github.funczz.ruby_calc.core.model.setting.SettingItems
import com.github.funczz.ruby_calc.core.usecase.setting.ProgramEditSaveSettingUseCase
import com.github.funczz.ruby_calc.core.usecase.setting.SaveSettingUseCase

class ProgramEditSaveSettingInteractor(

    private val useCase: SaveSettingUseCase

) : ProgramEditSaveSettingUseCase {

    override fun invoke(inputData: ProgramEditSaveSettingUseCase.InputData) {
        //id
        useCase(
            inputData = SaveSettingUseCase.InputData(
                settingName = SettingItems.PROGRAM_EDIT_ID.name,
                values = listOf(inputData.programEdit.id?.toString() ?: ""),
            )
        )
        //name
        useCase(
            inputData = SaveSettingUseCase.InputData(
                settingName = SettingItems.PROGRAM_EDIT_NAME.name,
                values = listOf(inputData.programEdit.name),
            )
        )
        //description
        useCase(
            inputData = SaveSettingUseCase.InputData(
                settingName = SettingItems.PROGRAM_EDIT_DESCRIPTION.name,
                values = listOf(inputData.programEdit.description),
            )
        )
        //hint
        useCase(
            inputData = SaveSettingUseCase.InputData(
                settingName = SettingItems.PROGRAM_EDIT_HINT.name,
                values = listOf(inputData.programEdit.hint),
            )
        )
        //code
        useCase(
            inputData = SaveSettingUseCase.InputData(
                settingName = SettingItems.PROGRAM_EDIT_CODE.name,
                values = listOf(inputData.programEdit.code),
            )
        )
    }

}
