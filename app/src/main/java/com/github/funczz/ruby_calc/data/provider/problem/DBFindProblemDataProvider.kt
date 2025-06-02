package com.github.funczz.ruby_calc.data.provider.problem

import com.github.funczz.ruby_calc.core.data.provider.problem.FindProblemDataProvider
import com.github.funczz.ruby_calc.core.model.problem.ProblemModel
import com.github.funczz.ruby_calc.core.vo.Column
import com.github.funczz.ruby_calc.core.vo.OrderBy
import com.github.funczz.ruby_calc.data.db.entity.getModel
import com.github.funczz.ruby_calc.data.db.table.ProblemTable
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

class DBFindProblemDataProvider(

    private val database: Database

) : FindProblemDataProvider, DatabaseExt {

    override fun find(
        programId: Long?,
        value: String,
        orderColumn: Column,
        orderBy: OrderBy,
        limit: Int
    ): List<ProblemModel> {
        val column = when (orderColumn) {
            Column.NAME -> ProblemTable.name
            Column.CREATED_AT -> ProblemTable.createdAt
            Column.UPDATED_AT -> ProblemTable.updatedAt
        }
        val resultSet = database.from(ProblemTable)
            .select()
            .let {
                programId?.let { id ->
                    it.where { (ProblemTable.programId eq id) and (ProblemTable.name like value) }
                } ?: it.where { ProblemTable.name like value }
            }
            .orderBy(if (orderBy == OrderBy.ASC) column.asc() else column.desc())
        val problemList = mutableListOf<ProblemModel>()
        for ((index, row) in resultSet.withIndex()) {
            if (index + 1 > limit) break
            problemList.add(ProblemTable.createEntity(row).getModel())
        }
        return problemList
    }

}
