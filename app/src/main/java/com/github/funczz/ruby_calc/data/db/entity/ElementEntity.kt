package com.github.funczz.ruby_calc.data.db.entity

import com.github.funczz.ruby_calc.core.model.element.ElementModel
import com.github.funczz.ruby_calc.data.db.table.ElementTable
import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf

interface ElementEntity : Entity<ElementEntity> {
    companion object : Entity.Factory<ElementEntity>()

    var problemId: Long

    var elementIndex: Int

    var elementValue: String

}

fun ElementEntity.getModel(): ElementModel {
    return ElementModel(
        problemId = this.problemId,
        elementIndex = this.elementIndex,
        elementValue = this.elementValue,
    )
}

fun ElementEntity.compareTo(
    problemId: Long,
    elementIndex: Int,
    elementValue: String,
): Boolean {
    if (this.problemId != problemId) return false
    if (this.elementIndex != elementIndex) return false
    if (this.elementValue != elementValue) return false
    return true
}

val Database.element get() = this.sequenceOf(ElementTable)
