package com.github.funczz.ruby_calc.data.db.table

import com.github.funczz.ruby_calc.data.db.entity.ElementEntity
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.long
import org.ktorm.schema.varchar

object ElementTable : Table<ElementEntity>("problem_element") {

    val problemId = long("problem_id").primaryKey().bindTo { it.problemId }

    val elementIndex = int("element_index").primaryKey().bindTo { it.elementIndex }

    val elementValue = varchar("element_value").bindTo { it.elementValue }

}
