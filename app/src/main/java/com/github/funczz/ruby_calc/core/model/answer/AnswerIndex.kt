package com.github.funczz.ruby_calc.core.model.answer

import com.github.funczz.ruby_calc.core.vo.Limit
import com.github.funczz.ruby_calc.core.vo.OrderBy
import java.util.Optional

data class AnswerIndex(
    val answerList: List<AnswerModel> = listOf(),
    val problemId: Optional<Long> = Optional.empty(),
    val value: String = "%",
    val orderBy: OrderBy = OrderBy.ASC,
    val limit: Int = Limit.VALUE,
)
