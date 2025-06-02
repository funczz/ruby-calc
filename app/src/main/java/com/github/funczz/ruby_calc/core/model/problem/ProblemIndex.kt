package com.github.funczz.ruby_calc.core.model.problem

import com.github.funczz.ruby_calc.core.vo.Column
import com.github.funczz.ruby_calc.core.vo.Limit
import com.github.funczz.ruby_calc.core.vo.OrderBy

data class ProblemIndex(
    val problemList: List<ProblemModel> = listOf(),
    val programId: Long? = null,
    val value: String = "%",
    val orderColumn: Column = Column.NAME,
    val orderBy: OrderBy = OrderBy.ASC,
    val limit: Int = Limit.VALUE,
)
