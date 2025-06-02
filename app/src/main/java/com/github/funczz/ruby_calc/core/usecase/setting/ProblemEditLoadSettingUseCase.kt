package com.github.funczz.ruby_calc.core.usecase.setting

import com.github.funczz.ruby_calc.core.model.problem.ProblemEdit

interface ProblemEditLoadSettingUseCase :
    SettingUseCase<ProblemEditLoadSettingUseCase.InputData, ProblemEdit> {

    data class InputData(
        val problemEdit: ProblemEdit
    ) : SettingUseCase.InputData

}