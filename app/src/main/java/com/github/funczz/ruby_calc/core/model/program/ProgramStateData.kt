package com.github.funczz.ruby_calc.core.model.program

import com.github.funczz.ruby_calc.core.model.RubyCalcStateData
import com.github.funczz.ruby_calc.core.usecase.program.GetListProgramUseCase
import com.github.funczz.ruby_calc.core.usecase.program.ProgramUseCase
import com.github.funczz.ruby_calc.core.vo.Column
import com.github.funczz.ruby_calc.core.vo.OrderBy

sealed interface ProgramStateData : RubyCalcStateData {

    data class InitializeData(

        val programDetails: ProgramDetails? = null,

        val programIndex: ProgramIndex? = null,

        val programSaveResult: ProgramSaveResult? = null,

        val programSelection: ProgramIndex? = null,

        ) : ProgramStateData

    data class SelectionData(

        val value: GetListProgramUseCase.InputData

    ) : ProgramStateData {
        companion object {
            @JvmStatic
            fun new(
                value: String,
                orderColumn: Column,
                orderBy: OrderBy,
                limit: Int,
            ): SelectionData = SelectionData(
                value = GetListProgramUseCase.InputData(
                    value = value,
                    orderColumn = orderColumn,
                    orderBy = orderBy,
                    limit = limit,
                )
            )
        }
    }

    data class InputData(
        val value: ProgramUseCase.InputData
    ) : ProgramStateData

    data object LoadData : ProgramStateData

    data object SaveData : ProgramStateData

}
