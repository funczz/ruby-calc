package com.github.funczz.ruby_calc.data.provider.answer

import com.github.funczz.ruby_calc.core.data.provider.answer.CRUDAnswerDataProvider
import com.github.funczz.ruby_calc.core.model.answer.AnswerModel
import com.github.funczz.ruby_calc.data.db.entity.AnswerEntity
import com.github.funczz.ruby_calc.data.db.entity.answer
import com.github.funczz.ruby_calc.data.db.entity.getModel
import com.github.funczz.ruby_calc.data.db.util.DatabaseExt
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.entity.add
import org.ktorm.entity.count
import org.ktorm.entity.filter
import org.ktorm.entity.find
import org.ktorm.entity.removeIf
import java.sql.Timestamp
import java.time.Instant
import java.util.Optional

class DBCRUDAnswerDataProvider(

    database: Database

) : CRUDAnswerDataProvider, DatabaseExt {

    private val entitySequence = database.answer

    override fun count(problemId: Long): Int {
        return entitySequence.filter { it.problemId eq problemId }.count()
    }

    override fun create(problemId: Long, value: String): Int {
        var result = 0
        entitySequence.database.commit {
            result = entitySequence.add(
                entity = AnswerEntity {
                    this.problemId = problemId
                    this.value = value
                }
            )
        }
        return result
    }

    override fun create(id: Long, problemId: Long, value: String, createdAt: Instant?): Int {
        var result = 0
        entitySequence.database.commit {
            result = entitySequence.add(
                entity = AnswerEntity {
                    this.id = id
                    this.problemId = problemId
                    this.value = value
                    createdAt?.let { this.createdAt = Timestamp(it.toEpochMilli()) }
                }
            )
        }
        return result
    }

    override fun read(id: Long): Optional<AnswerModel> {
        return Optional.ofNullable(
            entitySequence.find { it.id eq id }?.getModel()
        )
    }

    override fun delete(id: Long): Int {
        var result = 0
        entitySequence.database.commit {
            result = entitySequence.removeIf { it.id eq id }
        }
        return result
    }
}
