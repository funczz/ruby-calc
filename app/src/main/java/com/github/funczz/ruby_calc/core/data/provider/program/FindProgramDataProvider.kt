package com.github.funczz.ruby_calc.core.data.provider.program

import com.github.funczz.ruby_calc.core.model.program.ProgramModel
import com.github.funczz.ruby_calc.core.vo.Column
import com.github.funczz.ruby_calc.core.vo.Limit
import com.github.funczz.ruby_calc.core.vo.OrderBy

interface FindProgramDataProvider {

    fun find(
        value: String = "%",
        orderColumn: Column = Column.NAME,
        orderBy: OrderBy = OrderBy.ASC,
        limit: Int = Limit.VALUE
    ): List<ProgramModel>

}
