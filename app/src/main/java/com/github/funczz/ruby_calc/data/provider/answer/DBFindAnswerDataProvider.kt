package com.github.funczz.ruby_calc.data.provider.answer

import com.github.funczz.ruby_calc.core.data.provider.answer.FindAnswerDataProvider
import com.github.funczz.ruby_calc.core.model.answer.AnswerModel
import com.github.funczz.ruby_calc.core.vo.OrderBy
import com.github.funczz.ruby_calc.data.db.entity.getModel
import com.github.funczz.ruby_calc.data.db.table.AnswerTable
import com.github.funczz.ruby_calc.data.db.util.DatabaseExt
import org.ktorm.database.Database
import org.ktorm.dsl.and
import org.ktorm.dsl.asc
import org.ktorm.dsl.desc
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.like
import org.ktorm.dsl.orderBy
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import org.ktorm.dsl.withIndex

class DBFindAnswerDataProvider(

    private val database: Database

) : FindAnswerDataProvider, DatabaseExt {

    override fun find(
        problemId: Long,
        value: String,
        orderBy: OrderBy,
        limit: Int
    ): List<AnswerModel> {
        val column = AnswerTable.createdAt
        val resultSet = database.from(AnswerTable)
            .select()
            .where { (AnswerTable.problemId eq problemId) and (AnswerTable.value like value) }
            .orderBy(if (orderBy == OrderBy.ASC) column.asc() else column.desc())
        val answerList = mutableListOf<AnswerModel>()
        for ((index, row) in resultSet.withIndex()) {
            if (index + 1 > limit) break
            answerList.add(AnswerTable.createEntity(row).getModel())
        }
        return answerList
    }
}
