package com.github.funczz.ruby_calc.core.usecase.answer

import com.github.funczz.ruby_calc.core.model.answer.AnswerModel
import java.util.Optional

interface GetDetailsAnswerUseCase :
    AnswerUseCase<GetDetailsAnswerUseCase.InputData, Optional<AnswerModel>> {

    data class InputData(
        val id: Long
    ) : AnswerUseCase.InputData

}
