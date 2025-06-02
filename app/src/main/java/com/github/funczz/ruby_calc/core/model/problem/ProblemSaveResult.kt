package com.github.funczz.ruby_calc.core.model.problem

import java.util.Optional

data class ProblemSaveResult(
    val existsName: Boolean = false,
    val id: Optional<Long> = Optional.empty(),
)
