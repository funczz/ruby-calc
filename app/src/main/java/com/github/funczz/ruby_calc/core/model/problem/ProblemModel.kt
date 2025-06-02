package com.github.funczz.ruby_calc.core.model.problem

import java.time.Instant

data class ProblemModel(

    val id: Long,

    val name: String,

    val programId: Long,

    val comment: String,

    val createdAt: Instant,

    val updatedAt: Instant,

    )
