package com.github.funczz.ruby_calc.core.model.program

import java.util.Optional

data class ProgramDetails(
    val programModel: Optional<ProgramModel> = Optional.empty(),
    val canBeDeleted: Boolean = false,
)
