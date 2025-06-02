package com.github.funczz.ruby_calc.core.usecase.answer

import com.github.funczz.ruby_calc.core.model.answer.AnswerModel
import com.github.funczz.ruby_calc.core.vo.Limit
import com.github.funczz.ruby_calc.core.vo.OrderBy

interface GetListAnswerUseCase :
    AnswerUseCase<GetListAnswerUseCase.InputData, List<AnswerModel>> {

    data class InputData(
        val problemId: Long,
        val value: String = "%",
        val orderBy: OrderBy = OrderBy.ASC,
        val limit: Int = Limit.VALUE,
    ) : AnswerUseCase.InputData

}
