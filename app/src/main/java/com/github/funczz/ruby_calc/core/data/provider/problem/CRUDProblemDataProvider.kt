package com.github.funczz.ruby_calc.core.data.provider.problem

import com.github.funczz.ruby_calc.core.model.problem.ProblemModel
import java.time.Instant
import java.util.Optional

interface CRUDProblemDataProvider {

    fun count(): Int

    fun create(name: String, programId: Long, comment: String): Int

    fun create(
        id: Long,
        name: String,
        programId: Long,
        comment: String,
        createdAt: Instant?,
        updatedAt: Instant?,
    ): Int

    fun read(id: Long): Optional<ProblemModel>

    fun read(name: String): Optional<ProblemModel>

    fun update(
        id: Long,
        name: String? = null,
        programId: Long? = null,
        comment: String? = null,
    ): Int

    fun delete(id: Long): Int

}
