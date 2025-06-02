package com.github.funczz.ruby_calc.core.usecase.setting

import java.util.Optional

interface ProgramDetailsSaveSettingUseCase :
    SettingUseCase<ProgramDetailsSaveSettingUseCase.InputData, Unit> {

    data class InputData(
        val programId: Optional<Long>
    ) : SettingUseCase.InputData

}
