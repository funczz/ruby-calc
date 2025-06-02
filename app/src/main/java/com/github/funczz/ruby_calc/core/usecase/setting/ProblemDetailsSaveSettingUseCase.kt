package com.github.funczz.ruby_calc.core.usecase.setting

import java.util.Optional

interface ProblemDetailsSaveSettingUseCase :
    SettingUseCase<ProblemDetailsSaveSettingUseCase.InputData, Unit> {

    data class InputData(
        val problemId: Optional<Long>
    ) : SettingUseCase.InputData

}
