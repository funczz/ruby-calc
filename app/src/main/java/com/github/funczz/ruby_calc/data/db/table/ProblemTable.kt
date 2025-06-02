package com.github.funczz.ruby_calc.data.db.table

import com.github.funczz.ruby_calc.data.db.entity.ProblemEntity
import org.ktorm.schema.Table
import org.ktorm.schema.jdbcTimestamp
import org.ktorm.schema.long
import org.ktorm.schema.varchar

object ProblemTable : Table<ProblemEntity>("problem") {

    val id = long("problem_id").primaryKey().bindTo { it.id }

    val name = varchar("problem_name").bindTo { it.name }

    val programId = long("program_id").bindTo { it.programId }

    val comment = varchar("comment").bindTo { it.comment }

    val createdAt = jdbcTimestamp("created_at").bindTo { it.createdAt }

    val updatedAt = jdbcTimestamp("updated_at").bindTo { it.updatedAt }

}
