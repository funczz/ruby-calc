package com.github.funczz.ruby_calc.data.db.table

import com.github.funczz.ruby_calc.data.db.entity.ProgramEntity
import org.ktorm.schema.Table
import org.ktorm.schema.jdbcTimestamp
import org.ktorm.schema.long
import org.ktorm.schema.varchar

object ProgramTable : Table<ProgramEntity>("program") {

    val id = long("program_id").primaryKey().bindTo { it.id }

    val name = varchar("program_name").bindTo { it.name }

    val description = varchar("description").bindTo { it.description }

    val hint = varchar("hint").bindTo { it.hint }

    val code = varchar("code").bindTo { it.code }

    val createdAt = jdbcTimestamp("created_at").bindTo { it.createdAt }

    val updatedAt = jdbcTimestamp("updated_at").bindTo { it.updatedAt }

}
