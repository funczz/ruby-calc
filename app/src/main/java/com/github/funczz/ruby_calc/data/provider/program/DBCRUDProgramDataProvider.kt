package com.github.funczz.ruby_calc.data.provider.program

import com.github.funczz.ruby_calc.core.data.provider.program.CRUDProgramDataProvider
import com.github.funczz.ruby_calc.core.model.program.ProgramModel
import com.github.funczz.ruby_calc.data.db.entity.ProgramEntity
import com.github.funczz.ruby_calc.data.db.entity.getModel
import com.github.funczz.ruby_calc.data.db.entity.program
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

class DBCRUDProgramDataProvider(

    database: Database

) : CRUDProgramDataProvider, DatabaseExt {

    private val entitySequence = database.program

    override fun count(): Int = entitySequence.count()

    override fun create(name: String, description: String, hint: String, code: String): Int {
        var result = 0
        entitySequence.database.commit {
            result = entitySequence.add(
                entity = ProgramEntity {
                    this.name = name
                    this.description = description
                    this.hint = hint
                    this.code = code
                }
            )
        }
        return result
    }

    override fun create(
        id: Long,
        name: String,
        description: String,
        hint: String,
        code: String,
        createdAt: Instant?,
        updatedAt: Instant?,
    ): Int {
        var result = 0
        entitySequence.database.commit {
            result = entitySequence.add(
                entity = ProgramEntity {
                    this.id = id
                    this.name = name
                    this.description = description
                    this.hint = hint
                    this.code = code
                    createdAt?.let { this.createdAt = Timestamp(it.toEpochMilli()) }
                    updatedAt?.let { this.updatedAt = Timestamp(it.toEpochMilli()) }
                }
            )
        }
        return result
    }

    override fun read(id: Long): Optional<ProgramModel> {
        return Optional.ofNullable(
            entitySequence.find { it.id eq id }?.getModel()
        )
    }

    override fun read(name: String): Optional<ProgramModel> {
        return Optional.ofNullable(
            entitySequence.find { it.name eq name }?.getModel()
        )
    }

    override fun update(
        id: Long,
        name: String?,
        description: String?,
        hint: String?,
        code: String?,
    ): Int {
        var result = 0
        entitySequence.database.commit {
            entitySequence.find { it.id eq id }?.also { e ->
                name?.let { e.name = it }
                description?.let { e.description = it }
                hint?.let { e.hint = it }
                code?.let { e.code = it }
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
            //entitySequence.find { it.id eq id }?.delete()
        }
        return result
    }
}
