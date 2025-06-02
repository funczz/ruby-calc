package com.github.funczz.ruby_calc.core.usecase.setting

import com.github.funczz.ruby_calc.core.model.program.ProgramEdit

interface ProgramEditLoadSettingUseCase :
    SettingUseCase<ProgramEditLoadSettingUseCase.InputData, ProgramEdit> {

    data class InputData(
        val programEdit: ProgramEdit
    ) : SettingUseCase.InputData

}
