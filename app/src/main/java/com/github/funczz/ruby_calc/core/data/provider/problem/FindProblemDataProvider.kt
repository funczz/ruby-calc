package com.github.funczz.ruby_calc.core.data.provider.problem

import com.github.funczz.ruby_calc.core.model.problem.ProblemModel
import com.github.funczz.ruby_calc.core.vo.Column
import com.github.funczz.ruby_calc.core.vo.Limit
import com.github.funczz.ruby_calc.core.vo.OrderBy

interface FindProblemDataProvider {

    fun find(
        programId: Long? = null,
        value: String = "%",
        orderColumn: Column = Column.NAME,
        orderBy: OrderBy = OrderBy.ASC,
        limit: Int = Limit.VALUE
    ): List<ProblemModel>

}
