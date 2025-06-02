package com.github.funczz.ruby_calc.android.destination.problem

import com.github.funczz.ruby_calc.android.TextFieldState
import com.github.funczz.ruby_calc.core.model.program.ProgramModel
import java.time.Instant

data class ProblemEditUiState(
    val id: Long? = null,
    val name: TextFieldState = TextFieldState(text = ""),
    val programModel: ProgramModel? = null,
    val comment: TextFieldState = TextFieldState(text = ""),
    val createAt: Instant? = null,
    val updatedAt: Instant? = null,
)
