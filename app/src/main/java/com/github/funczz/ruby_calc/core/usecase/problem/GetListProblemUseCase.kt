package com.github.funczz.ruby_calc.core.usecase.problem

import com.github.funczz.ruby_calc.core.model.problem.ProblemModel
import com.github.funczz.ruby_calc.core.vo.Column
import com.github.funczz.ruby_calc.core.vo.Limit
import com.github.funczz.ruby_calc.core.vo.OrderBy

interface GetListProblemUseCase :
    ProblemUseCase<GetListProblemUseCase.InputData, List<ProblemModel>> {

    data class InputData(
        val programId: Long? = null,
        val value: String = "%",
        val orderColumn: Column = Column.NAME,
        val orderBy: OrderBy = OrderBy.ASC,
        val limit: Int = Limit.VALUE,
    ) : ProblemUseCase.InputData

}
