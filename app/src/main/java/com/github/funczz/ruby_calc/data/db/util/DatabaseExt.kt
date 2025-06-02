package com.github.funczz.ruby_calc.data.db.util

import com.github.funczz.ruby_calc.data.db.table.AnswerTable
import com.github.funczz.ruby_calc.data.db.table.ElementTable
import com.github.funczz.ruby_calc.data.db.table.ProblemTable
import com.github.funczz.ruby_calc.data.db.table.ProgramTable
import org.ktorm.database.Database
import org.ktorm.dsl.asc
import org.ktorm.dsl.from
import org.ktorm.dsl.insert
import org.ktorm.dsl.like
import org.ktorm.dsl.orderBy
import org.ktorm.dsl.select
import org.ktorm.dsl.where

interface DatabaseExt {

    fun Database.commit(function: () -> Unit) {
        this.useTransaction {
            try {
                function()
                it.commit()
            } catch (th: Throwable) {
                it.rollback()
                if (th !is CancellationException) {
                    throw th
                }
            }
        }
    }

    fun Database.clone(cloneDatabase: Database) {
        cloneDatabase.useTransaction {
            //ProgramTable
            for (row in this
                .from(ProgramTable)
                .select()
                .where { ProgramTable.name like "%" }
                .orderBy(ProgramTable.id.asc())) {
                cloneDatabase.insert(ProgramTable) { tbl ->
                    set(tbl.id, row[tbl.id])
                    set(tbl.name, row[tbl.name])
                    set(tbl.description, row[tbl.description])
                    set(tbl.hint, row[tbl.hint])
                    set(tbl.code, row[tbl.code])
                    set(tbl.createdAt, row[tbl.createdAt])
                    set(tbl.updatedAt, row[tbl.updatedAt])
                }
                it.commit()
            }

            //ProblemTable
            for (row in this
                .from(ProblemTable)
                .select()
                .where { ProblemTable.name like "%" }
                .orderBy(ProblemTable.id.asc())) {
                cloneDatabase.insert(ProblemTable) { tbl ->
                    set(tbl.id, row[tbl.id])
                    set(tbl.name, row[tbl.name])
                    set(tbl.programId, row[tbl.programId])
                    set(tbl.comment, row[tbl.comment])
                    set(tbl.createdAt, row[tbl.createdAt])
                    set(tbl.updatedAt, row[tbl.updatedAt])
                }
                it.commit()
            }

            //ElementTable
            for (row in this
                .from(ElementTable)
                .select()
                .where { ElementTable.elementValue like "%" }
                .orderBy(ElementTable.problemId.asc(), ElementTable.elementIndex.asc())) {
                cloneDatabase.insert(ElementTable) { tbl ->
                    set(tbl.problemId, row[tbl.problemId])
                    set(tbl.elementIndex, row[tbl.elementIndex])
                    set(tbl.elementValue, row[tbl.elementValue])
                }
                it.commit()
            }

            //AnswerTable
            for (row in this
                .from(AnswerTable)
                .select()
                .where { AnswerTable.value like "%" }
                .orderBy(AnswerTable.id.asc())) {
                cloneDatabase.insert(AnswerTable) { tbl ->
                    set(tbl.id, row[tbl.id])
                    set(tbl.problemId, row[tbl.problemId])
                    set(tbl.value, row[tbl.value])
                    set(tbl.createdAt, row[tbl.createdAt])
                }
                it.commit()
            }
        }
    }

    open class CancellationException(message: String, cause: Throwable? = null) :
        Exception(message, cause) {
        companion object {
            private const val serialVersionUID: Long = -2494813656549325531L
        }
    }

}
