package com.github.funczz.ruby_calc.core.model.problem

import com.github.funczz.ruby_calc.core.model.RubyCalcStateData
import com.github.funczz.ruby_calc.core.usecase.problem.ProblemUseCase

sealed interface ProblemStateData : RubyCalcStateData {

    data class InitializeData(

        val problemDetails: ProblemDetails? = null,

        val problemIndex: ProblemIndex? = null,

        val problemSaveResult: ProblemSaveResult? = null,

        val problemEdit: ProblemEdit? = null,

        ) : ProblemStateData

    data class InputData(
        val value: ProblemUseCase.InputData
    ) : ProblemStateData

    data object LoadData : ProblemStateData

    data object SaveData : ProblemStateData
}
