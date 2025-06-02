package com.github.funczz.ruby_calc.core.interactor.setting

import com.github.funczz.ruby_calc.core.model.program.ProgramEdit
import com.github.funczz.ruby_calc.core.model.setting.SettingItems
import com.github.funczz.ruby_calc.core.usecase.setting.LoadSettingUseCase
import com.github.funczz.ruby_calc.core.usecase.setting.ProgramEditLoadSettingUseCase

class ProgramEditLoadSettingInteractor(

    private val useCase: LoadSettingUseCase

) : ProgramEditLoadSettingUseCase {

    override fun invoke(inputData: ProgramEditLoadSettingUseCase.InputData): ProgramEdit {
        //id
        val id = useCase(
            inputData = LoadSettingUseCase.InputData(settingName = SettingItems.PROGRAM_EDIT_ID.name)
        ).let {
            try {
                it.firstOrNull()?.settingValue?.toLong() ?: inputData.programEdit.id
            } catch (th: NumberFormatException) {
                inputData.programEdit.id
            }
        }
        //name
        val name = useCase(
            inputData = LoadSettingUseCase.InputData(settingName = SettingItems.PROGRAM_EDIT_NAME.name)
        ).let {
            it.firstOrNull()?.settingValue ?: inputData.programEdit.name
        }
        //description
        val description = useCase(
            inputData = LoadSettingUseCase.InputData(settingName = SettingItems.PROGRAM_EDIT_DESCRIPTION.name)
        ).let {
            it.firstOrNull()?.settingValue ?: inputData.programEdit.description
        }
        //hint
        val hint = useCase(
            inputData = LoadSettingUseCase.InputData(settingName = SettingItems.PROGRAM_EDIT_HINT.name)
        ).let {
            it.firstOrNull()?.settingValue ?: inputData.programEdit.hint
        }
        //code
        val code = useCase(
            inputData = LoadSettingUseCase.InputData(settingName = SettingItems.PROGRAM_EDIT_CODE.name)
        ).let {
            it.firstOrNull()?.settingValue ?: inputData.programEdit.code
        }

        return ProgramEdit(
            id = id,
            name = name,
            description = description,
            hint = hint,
            code = code,
        )
    }

}
