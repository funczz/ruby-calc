package com.github.funczz.ruby_calc.core.data.provider.element

import com.github.funczz.ruby_calc.core.model.element.ElementModel

interface RWElementDataProvider {

    fun read(problemId: Long): List<ElementModel>

    fun write(problemId: Long, values: List<String>): Int

}
