package com.github.funczz.ruby_calc.data.provider.element

import com.github.funczz.ruby_calc.core.data.provider.element.RWElementDataProvider
import com.github.funczz.ruby_calc.core.model.element.ElementModel
import com.github.funczz.ruby_calc.data.db.entity.ElementEntity
import com.github.funczz.ruby_calc.data.db.entity.compareTo
import com.github.funczz.ruby_calc.data.db.entity.element
import com.github.funczz.ruby_calc.data.db.entity.getModel
import com.github.funczz.ruby_calc.data.db.table.ElementTable
import com.github.funczz.ruby_calc.data.db.util.DatabaseExt
import org.ktorm.database.Database
import org.ktorm.dsl.and
import org.ktorm.dsl.asc
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.greaterEq
import org.ktorm.dsl.orderBy
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import org.ktorm.entity.add
import org.ktorm.entity.count
import org.ktorm.entity.filter
import org.ktorm.entity.find
import org.ktorm.entity.removeIf
import org.ktorm.entity.update

class DBRWElementDataProvider(

    database: Database

) : RWElementDataProvider, DatabaseExt {

    private val entitySequence = database.element

    override fun read(problemId: Long): List<ElementModel> {
        val resultSet = entitySequence.database.from(ElementTable)
            .select()
            .where { ElementTable.problemId eq problemId }
            .orderBy(ElementTable.elementIndex.asc())
        val elementList = mutableListOf<ElementModel>()
        for (row in resultSet) {
            elementList.add(ElementTable.createEntity(row).getModel())
        }
        return elementList
    }

    override fun write(problemId: Long, values: List<String>): Int {
        entitySequence.database.commit {
            entitySequence.removeIf { it.elementIndex greaterEq values.size }

            for ((index, value) in values.withIndex()) {
                val element = entitySequence.find {
                    (it.problemId eq problemId) and (it.elementIndex eq index)
                }
                when (element) {
                    is ElementEntity -> if (!element.compareTo(
                            problemId = problemId,
                            elementIndex = index,
                            elementValue = value,
                        )
                    ) {
                        element.elementValue = value
                        entitySequence.update(entity = element)
                    }

                    else -> entitySequence.add(
                        entity = ElementEntity {
                            this.problemId = problemId
                            this.elementIndex = index
                            this.elementValue = value
                        }
                    )
                }
            }
        }
        return entitySequence.filter { it.problemId eq problemId }.count()
    }
}
