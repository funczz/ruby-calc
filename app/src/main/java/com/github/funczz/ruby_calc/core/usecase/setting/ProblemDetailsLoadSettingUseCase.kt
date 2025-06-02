package com.github.funczz.ruby_calc.core.usecase.setting

import java.util.Optional

interface ProblemDetailsLoadSettingUseCase :
    SettingUseCase<ProblemDetailsLoadSettingUseCase.InputData, Optional<Long>> {

    data class InputData(
        val defaultId: Optional<Long> = Optional.empty()
    ) : SettingUseCase.InputData

}
