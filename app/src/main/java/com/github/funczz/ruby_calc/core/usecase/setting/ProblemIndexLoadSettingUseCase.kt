package com.github.funczz.ruby_calc.core.usecase.setting

import com.github.funczz.ruby_calc.core.model.problem.ProblemIndex

interface ProblemIndexLoadSettingUseCase :
    SettingUseCase<ProblemIndexLoadSettingUseCase.InputData, ProblemIndex> {

    data class InputData(
        val problemIndex: ProblemIndex
    ) : SettingUseCase.InputData

}