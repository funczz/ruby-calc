package com.github.funczz.ruby_calc.core.interactor.setting

import com.github.funczz.ruby_calc.core.model.setting.SettingItems
import com.github.funczz.ruby_calc.core.usecase.setting.ProblemEditSaveSettingUseCase
import com.github.funczz.ruby_calc.core.usecase.setting.SaveSettingUseCase

class ProblemEditSaveSettingInteractor(

    private val useCase: SaveSettingUseCase

) : ProblemEditSaveSettingUseCase {

    override fun invoke(inputData: ProblemEditSaveSettingUseCase.InputData) {
        //id
        useCase(
            inputData = SaveSettingUseCase.InputData(
                settingName = SettingItems.PROBLEM_EDIT_ID.name,
                values = listOf(inputData.problemEdit.id?.toString() ?: ""),
            )
        )
        //name
        useCase(
            inputData = SaveSettingUseCase.InputData(
                settingName = SettingItems.PROBLEM_EDIT_NAME.name,
                values = listOf(inputData.problemEdit.name),
            )
        )
        //programId
        useCase(
            inputData = SaveSettingUseCase.InputData(
                settingName = SettingItems.PROBLEM_EDIT_PROGRAM_ID.name,
                values = listOf(inputData.problemEdit.programId?.toString() ?: ""),
            )
        )
        //programName
        useCase(
            inputData = SaveSettingUseCase.InputData(
                settingName = SettingItems.PROBLEM_EDIT_PROGRAM_NAME.name,
                values = listOf(inputData.problemEdit.programName),
            )
        )
        //programDescription
        useCase(
            inputData = SaveSettingUseCase.InputData(
                settingName = SettingItems.PROBLEM_EDIT_PROGRAM_DESCRIPTION.name,
                values = listOf(inputData.problemEdit.programDescription),
            )
        )
        //programHint
        useCase(
            inputData = SaveSettingUseCase.InputData(
                settingName = SettingItems.PROBLEM_EDIT_PROGRAM_HINT.name,
                values = listOf(inputData.problemEdit.programHint),
            )
        )
        //programCode
        useCase(
            inputData = SaveSettingUseCase.InputData(
                settingName = SettingItems.PROBLEM_EDIT_PROGRAM_CODE.name,
                values = listOf(inputData.problemEdit.programCode),
            )
        )
        //programCreatedAt
        useCase(
            inputData = SaveSettingUseCase.InputData(
                settingName = SettingItems.PROBLEM_EDIT_PROGRAM_CREATED_AT.name,
                values = listOf(inputData.problemEdit.programCreatedAt.toEpochMilli().toString()),
            )
        )
        //programUpdatedAt
        useCase(
            inputData = SaveSettingUseCase.InputData(
                settingName = SettingItems.PROBLEM_EDIT_PROGRAM_UPDATED_AT.name,
                values = listOf(inputData.problemEdit.programUpdatedAt.toEpochMilli().toString()),
            )
        )
        //comment
        useCase(
            inputData = SaveSettingUseCase.InputData(
                settingName = SettingItems.PROBLEM_EDIT_COMMENT.name,
                values = listOf(inputData.problemEdit.comment),
            )
        )
    }

}
