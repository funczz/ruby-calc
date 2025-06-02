package com.github.funczz.ruby_calc.core.model.problem

import com.github.funczz.ruby_calc.core.model.program.ProgramModel
import java.util.Optional

data class ProblemDetails(
    val problemModel: Optional<ProblemModel> = Optional.empty(),
    val programModel: Optional<ProgramModel> = Optional.empty(),
    val canBeDeleted: Boolean = false,
)
