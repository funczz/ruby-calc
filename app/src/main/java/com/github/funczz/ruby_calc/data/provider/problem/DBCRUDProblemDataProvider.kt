package com.github.funczz.ruby_calc.data.provider.problem

import com.github.funczz.ruby_calc.core.data.provider.problem.CRUDProblemDataProvider
import com.github.funczz.ruby_calc.core.model.problem.ProblemModel
import com.github.funczz.ruby_calc.data.db.entity.ProblemEntity
import com.github.funczz.ruby_calc.data.db.entity.getModel
import com.github.funczz.ruby_calc.data.db.entity.problem
import com.github.funczz.ruby_calc.data.db.util.DatabaseExt
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.entity.add
import org.ktorm.entity.count
import org.ktorm.entity.find
import org.ktorm.entity.removeIf
import org.ktorm.entity.update
import java.sql.Timestamp
import java.time.Instant
import java.util.Optional

class DBCRUDProblemDataProvider(

    database: Database

) : CRUDProblemDataProvider, DatabaseExt {

    private val entitySequence = database.problem

    override fun count(): Int {
        return entitySequence.count()
    }

    override fun create(name: String, programId: Long, comment: String): Int {
        var result = 0
        entitySequence.database.commit {
            result = entitySequence.add(
                entity = ProblemEntity {
                    this.name = name
                    this.programId = programId
                    this.comment = comment
                }
            )
        }
        return result
    }

    override fun create(
        id: Long,
        name: String,
        programId: Long,
        comment: String,
        createdAt: Instant?,
        updatedAt: Instant?,
    ): Int {
        var result = 0
        entitySequence.database.commit {
            result = entitySequence.add(
                entity = ProblemEntity {
                    this.id = id
                    this.name = name
                    this.programId = programId
                    this.comment = comment
                    createdAt?.let { this.createdAt = Timestamp(it.toEpochMilli()) }
                    updatedAt?.let { this.updatedAt = Timestamp(it.toEpochMilli()) }
                }
            )
        }
        return result
    }

    override fun read(id: Long): Optional<ProblemModel> {
        return Optional.ofNullable(
            entitySequence.find { it.id eq id }?.getModel()
        )
    }

    override fun read(name: String): Optional<ProblemModel> {
        return Optional.ofNullable(
            entitySequence.find { it.name eq name }?.getModel()
        )
    }

    override fun update(id: Long, name: String?, programId: Long?, comment: String?): Int {
        var result = 0
        entitySequence.database.commit {
            entitySequence.find { it.id eq id }?.also { e ->
                name?.let { e.name = it }
                programId?.let { e.programId = it }
                comment?.let { e.comment = it }
                e.updatedAt = Timestamp(Instant.now().toEpochMilli())
                result = entitySequence.update(entity = e)
            }
        }
        return result
    }

    override fun delete(id: Long): Int {
        var result = 0
        entitySequence.database.commit {
            result = entitySequence.removeIf { it.id eq id }
        }
        return result
    }
}
