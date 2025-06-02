package com.github.funczz.ruby_calc.data.db.entity

import com.github.funczz.ruby_calc.core.model.answer.AnswerModel
import com.github.funczz.ruby_calc.data.db.table.AnswerTable
import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import java.sql.Timestamp

interface AnswerEntity : Entity<AnswerEntity> {
    companion object : Entity.Factory<AnswerEntity>()

    var id: Long

    var problemId: Long

    var value: String

    var createdAt: Timestamp

}

fun AnswerEntity.getModel(): AnswerModel {
    return AnswerModel(
        id = this.id,
        problemId = this.problemId,
        value = this.value,
        createdAt = this.createdAt.toInstant(),
    )
}

fun AnswerEntity.compareTo(
    id: Long,
    programId: Long,
    value: String,
): Boolean {
    if (this.id != id) return false
    if (this.problemId != programId) return false
    if (this.value != value) return false
    return true
}

val Database.answer get() = this.sequenceOf(AnswerTable)
