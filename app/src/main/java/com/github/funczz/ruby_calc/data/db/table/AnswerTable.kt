package com.github.funczz.ruby_calc.data.db.table

import com.github.funczz.ruby_calc.data.db.entity.AnswerEntity
import org.ktorm.schema.Table
import org.ktorm.schema.jdbcTimestamp
import org.ktorm.schema.long
import org.ktorm.schema.varchar

object AnswerTable : Table<AnswerEntity>("problem_answer") {

    val id = long("answer_id").primaryKey().bindTo { it.id }

    val problemId = long("problem_id").bindTo { it.problemId }

    val value = varchar("answer_value").bindTo { it.value }

    val createdAt = jdbcTimestamp("created_at").bindTo { it.createdAt }

}
