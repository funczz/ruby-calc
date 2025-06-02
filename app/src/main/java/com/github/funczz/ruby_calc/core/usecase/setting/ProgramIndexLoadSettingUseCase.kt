package com.github.funczz.ruby_calc.core.usecase.setting

import com.github.funczz.ruby_calc.core.model.program.ProgramIndex

interface ProgramIndexLoadSettingUseCase :
    SettingUseCase<ProgramIndexLoadSettingUseCase.InputData, ProgramIndex> {

    data class InputData(
        val programIndex: ProgramIndex
    ) : SettingUseCase.InputData

}