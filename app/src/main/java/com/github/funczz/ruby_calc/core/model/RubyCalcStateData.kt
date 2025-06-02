package com.github.funczz.ruby_calc.core.model

import com.github.funczz.ruby_calc.core.model.program.ProgramStateData

interface RubyCalcStateData {

    data class Bulk(
        val values: MutableList<RubyCalcStateData> = mutableListOf()
    ) : RubyCalcStateData {
        fun addStateData(function: () -> RubyCalcStateData): Bulk {
            values.add(function())
            return this
        }
    }

    object OnClose : RubyCalcStateData

    object OnLoad : RubyCalcStateData

    object OnSave : RubyCalcStateData

    data class InitializeData(
        val programInitializeData: ProgramStateData.InitializeData? = null,
    ) : RubyCalcStateData

}
