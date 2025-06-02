package com.github.funczz.ruby_calc.core.model.program

import java.util.Optional

data class ProgramSaveResult(
    val existsName: Boolean = false,
    val id: Optional<Long> = Optional.empty(),
)
