package com.github.funczz.ruby_calc.core.usecase.setting

import com.github.funczz.ruby_calc.core.model.program.ProgramIndex

interface ProgramIndexSaveSettingUseCase :
    SettingUseCase<ProgramIndexSaveSettingUseCase.InputData, Unit> {

    data class InputData(
        val programIndex: ProgramIndex
    ) : SettingUseCase.InputData

}