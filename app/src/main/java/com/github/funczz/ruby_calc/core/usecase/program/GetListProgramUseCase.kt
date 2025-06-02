package com.github.funczz.ruby_calc.core.usecase.program

import com.github.funczz.ruby_calc.core.model.program.ProgramModel
import com.github.funczz.ruby_calc.core.vo.Column
import com.github.funczz.ruby_calc.core.vo.Limit
import com.github.funczz.ruby_calc.core.vo.OrderBy

interface GetListProgramUseCase :
    ProgramUseCase<GetListProgramUseCase.InputData, List<ProgramModel>> {

    data class InputData(
        val value: String = "%",
        val orderColumn: Column = Column.NAME,
        val orderBy: OrderBy = OrderBy.ASC,
        val limit: Int = Limit.VALUE,
    ) : ProgramUseCase.InputData

}
