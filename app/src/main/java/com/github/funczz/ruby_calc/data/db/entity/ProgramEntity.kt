package com.github.funczz.ruby_calc.data.db.entity

import com.github.funczz.ruby_calc.core.model.program.ProgramModel
import com.github.funczz.ruby_calc.data.db.table.ProgramTable
import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import java.sql.Timestamp

interface ProgramEntity : Entity<ProgramEntity> {
    companion object : Entity.Factory<ProgramEntity>()

    var id: Long

    var name: String

    var description: String

    var hint: String

    var code: String

    var createdAt: Timestamp

    var updatedAt: Timestamp

}

fun ProgramEntity.getModel(): ProgramModel {
    return ProgramModel(
        id = this.id,
        name = this.name,
        description = this.description,
        hint = this.hint,
        code = this.code,
        createdAt = this.createdAt.toInstant(),
        updatedAt = this.updatedAt.toInstant(),
    )
}

fun ProgramEntity.compareTo(
    id: Long,
    name: String,
    description: String,
    hint: String,
    code: String,
): Boolean {
    if (this.id != id) return false
    if (this.name != name) return false
    if (this.description != description) return false
    if (this.hint != hint) return false
    if (this.code != code) return false
    return true
}

val Database.program get() = this.sequenceOf(ProgramTable)
