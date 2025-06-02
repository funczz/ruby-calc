package com.github.funczz.ruby_calc.core.model.program

import com.github.funczz.ruby_calc.core.vo.Column
import com.github.funczz.ruby_calc.core.vo.Limit
import com.github.funczz.ruby_calc.core.vo.OrderBy

data class ProgramIndex(
    val programList: List<ProgramModel> = listOf(),
    val value: String = "%",
    val orderColumn: Column = Column.NAME,
    val orderBy: OrderBy = OrderBy.ASC,
    val limit: Int = Limit.VALUE,
)
