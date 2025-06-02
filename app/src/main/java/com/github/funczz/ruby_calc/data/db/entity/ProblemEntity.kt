package com.github.funczz.ruby_calc.data.db.entity

import com.github.funczz.ruby_calc.core.model.problem.ProblemModel
import com.github.funczz.ruby_calc.data.db.table.ProblemTable
import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import java.sql.Timestamp

interface ProblemEntity : Entity<ProblemEntity> {
    companion object : Entity.Factory<ProblemEntity>()

    var id: Long

    var name: String

    var programId: Long

    var comment: String

    var createdAt: Timestamp

    var updatedAt: Timestamp

}

fun ProblemEntity.getModel(): ProblemModel {
    return ProblemModel(
        id = this.id,
        name = this.name,
        programId = this.programId,
        comment = this.comment,
        createdAt = this.createdAt.toInstant(),
        updatedAt = this.updatedAt.toInstant(),
    )
}

fun ProblemEntity.compareTo(
    id: Long,
    name: String,
    programId: Long,
    comment: String,
): Boolean {
    if (this.id != id) return false
    if (this.name != name) return false
    if (this.programId != programId) return false
    if (this.comment != comment) return false
    return true
}

val Database.problem get() = this.sequenceOf(ProblemTable)
