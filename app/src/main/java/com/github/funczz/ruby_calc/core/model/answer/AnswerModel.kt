package com.github.funczz.ruby_calc.core.model.answer

import java.time.Instant

data class AnswerModel(

    val id: Long,

    val problemId: Long,

    val value: String,

    val createdAt: Instant,

    )
