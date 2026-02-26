package com.github.funczz.ruby_calc.core.interactor.setting

import com.github.funczz.ruby_calc.core.model.problem.ProblemEdit
import com.github.funczz.ruby_calc.core.model.setting.SettingItems
import com.github.funczz.ruby_calc.core.usecase.setting.LoadSettingUseCase
import com.github.funczz.ruby_calc.core.usecase.setting.ProblemEditLoadSettingUseCase
import java.time.Instant

class ProblemEditLoadSettingInteractor(

    private val useCase: LoadSettingUseCase

) : ProblemEditLoadSettingUseCase {

    override fun invoke(inputData: ProblemEditLoadSettingUseCase.InputData): ProblemEdit {
        //id
        val id = useCase(
            inputData = LoadSettingUseCase.InputData(settingName = SettingItems.PROBLEM_EDIT_ID.name)
        ).let {
            try {
                it.firstOrNull()?.settingValue?.toLong() ?: inputData.problemEdit.id
            } catch (th: NumberFormatException) {
                inputData.problemEdit.id
            }
        }
        //name
        val name = useCase(
            inputData = LoadSettingUseCase.InputData(settingName = SettingItems.PROBLEM_EDIT_NAME.name)
        ).let {
            it.firstOrNull()?.settingValue ?: inputData.problemEdit.name
        }
        //programId
        val programId = useCase(
            inputData = LoadSettingUseCase.InputData(settingName = SettingItems.PROBLEM_EDIT_PROGRAM_ID.name)
        ).let {
            try {
                it.firstOrNull()?.settingValue?.toLong() ?: inputData.problemEdit.programId
            } catch (th: NumberFormatException) {
                inputData.problemEdit.programId
            }
        }
        //programName
        val programName = useCase(
            inputData = LoadSettingUseCase.InputData(settingName = SettingItems.PROBLEM_EDIT_PROGRAM_NAME.name)
        ).let {
            it.firstOrNull()?.settingValue ?: inputData.problemEdit.programName
        }
        //programDescription
        val programDescription = useCase(
            inputData = LoadSettingUseCase.InputData(settingName = SettingItems.PROBLEM_EDIT_PROGRAM_DESCRIPTION.name)
        ).let {
            it.firstOrNull()?.settingValue ?: inputData.problemEdit.programDescription
        }
        //programHint
        val programHint = useCase(
            inputData = LoadSettingUseCase.InputData(settingName = SettingItems.PROBLEM_EDIT_PROGRAM_HINT.name)
        ).let {
            it.firstOrNull()?.settingValue ?: inputData.problemEdit.programHint
        }
        //programCode
        val programCode = useCase(
            inputData = LoadSettingUseCase.InputData(settingName = SettingItems.PROBLEM_EDIT_PROGRAM_CODE.name)
        ).let {
            it.firstOrNull()?.settingValue ?: inputData.problemEdit.programCode
        }
        //programCreatedAt
        val programCreatedAt = useCase(
            inputData = LoadSettingUseCase.InputData(settingName = SettingItems.PROBLEM_EDIT_PROGRAM_CREATED_AT.name)
        ).firstOrNull()?.settingValue?.toLong()?.let { Instant.ofEpochMilli(it) }
            ?: inputData.problemEdit.programCreatedAt
        //programUpdatedAt
        val programUpdatedAt = useCase(
            inputData = LoadSettingUseCase.InputData(settingName = SettingItems.PROBLEM_EDIT_PROGRAM_UPDATED_AT.name)
        ).firstOrNull()?.settingValue?.toLong()?.let { Instant.ofEpochMilli(it) }
            ?: inputData.problemEdit.programUpdatedAt
        //comment
        val comment = useCase(
            inputData = LoadSettingUseCase.InputData(settingName = SettingItems.PROBLEM_EDIT_COMMENT.name)
        ).let {
            it.firstOrNull()?.settingValue ?: inputData.problemEdit.comment
        }

        return ProblemEdit(
            id = id,
            name = name,
            programId = programId,
            programName = programName,
            programDescription = programDescription,
            programHint = programHint,
            programCode = programCode,
            programCreatedAt = programCreatedAt,
            comment = comment,
        )
    }

}