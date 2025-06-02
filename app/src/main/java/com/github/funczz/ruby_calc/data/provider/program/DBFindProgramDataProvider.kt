package com.github.funczz.ruby_calc.data.provider.program

import com.github.funczz.ruby_calc.core.data.provider.program.FindProgramDataProvider
import com.github.funczz.ruby_calc.core.model.program.ProgramModel
import com.github.funczz.ruby_calc.core.vo.Column
import com.github.funczz.ruby_calc.core.vo.OrderBy
import com.github.funczz.ruby_calc.data.db.entity.getModel
import com.github.funczz.ruby_calc.data.db.table.ProgramTable
import com.github.funczz.ruby_calc.data.db.util.DatabaseExt
import org.ktorm.database.Database
import org.ktorm.dsl.asc
import org.ktorm.dsl.desc
import org.ktorm.dsl.from
import org.ktorm.dsl.like
import org.ktorm.dsl.orderBy
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import org.ktorm.dsl.withIndex

class DBFindProgramDataProvider(

    private val database: Database

) : FindProgramDataProvider, DatabaseExt {

    override fun find(
        value: String,
        orderColumn: Column,
        orderBy: OrderBy,
        limit: Int
    ): List<ProgramModel> {
        val column = when (orderColumn) {
            Column.NAME -> ProgramTable.name
            Column.CREATED_AT -> ProgramTable.createdAt
            Column.UPDATED_AT -> ProgramTable.updatedAt
        }
        val resultSet = database.from(ProgramTable)
            .select()
            .where { ProgramTable.name like value }
            .orderBy(if (orderBy == OrderBy.ASC) column.asc() else column.desc())
        val programList = mutableListOf<ProgramModel>()
        for ((index, row) in resultSet.withIndex()) {
            if (index + 1 > limit) break
            programList.add(ProgramTable.createEntity(row).getModel())
        }
        return programList
    }

}
