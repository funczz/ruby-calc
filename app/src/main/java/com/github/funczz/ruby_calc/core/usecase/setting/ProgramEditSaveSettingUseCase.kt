package com.github.funczz.ruby_calc.core.usecase.setting

import com.github.funczz.ruby_calc.core.model.program.ProgramEdit

interface ProgramEditSaveSettingUseCase :
    SettingUseCase<ProgramEditSaveSettingUseCase.InputData, Unit> {

    data class InputData(
        val programEdit: ProgramEdit
    ) : SettingUseCase.InputData

}
