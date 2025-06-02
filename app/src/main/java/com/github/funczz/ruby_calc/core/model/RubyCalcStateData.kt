package com.github.funczz.ruby_calc.core.model

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

}
