package com.github.funczz.ruby_calc.core.model.problem

import java.time.Instant

data class ProblemEdit(
    val id: Long? = null,
    val name: String = "",
    val programId: Long? = null,
    val programName: String = "",
    val programDescription: String = "",
    val programHint: String = "",
    val programCode: String = "",
    val programCreatedAt: Instant = Instant.now(),
    val programUpdatedAt: Instant = Instant.now(),
    val comment: String = "",
)
