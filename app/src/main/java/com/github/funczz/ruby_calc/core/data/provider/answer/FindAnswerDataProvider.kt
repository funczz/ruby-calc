package com.github.funczz.ruby_calc.core.data.provider.answer

import com.github.funczz.ruby_calc.core.model.answer.AnswerModel
import com.github.funczz.ruby_calc.core.vo.Limit
import com.github.funczz.ruby_calc.core.vo.OrderBy

interface FindAnswerDataProvider {

    fun find(
        problemId: Long,
        value: String = "%",
        orderBy: OrderBy = OrderBy.ASC,
        limit: Int = Limit.VALUE
    ): List<AnswerModel>

}
