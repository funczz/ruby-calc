package com.github.funczz.ruby_calc.core.data.provider.answer

import com.github.funczz.ruby_calc.core.model.answer.AnswerModel
import java.time.Instant
import java.util.Optional

interface CRUDAnswerDataProvider {

    fun count(problemId: Long): Int

    fun create(problemId: Long, value: String): Int

    fun create(
        id: Long,
        problemId: Long,
        value: String,
        createdAt: Instant?,
    ): Int

    fun read(id: Long): Optional<AnswerModel>

    fun delete(id: Long): Int
}
