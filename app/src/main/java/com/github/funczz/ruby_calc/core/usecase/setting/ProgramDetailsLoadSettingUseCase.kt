package com.github.funczz.ruby_calc.core.usecase.setting

import java.util.Optional

interface ProgramDetailsLoadSettingUseCase :
    SettingUseCase<ProgramDetailsLoadSettingUseCase.InputData, Optional<Long>> {

    data class InputData(
        val defaultId: Optional<Long> = Optional.empty()
    ) : SettingUseCase.InputData

}
