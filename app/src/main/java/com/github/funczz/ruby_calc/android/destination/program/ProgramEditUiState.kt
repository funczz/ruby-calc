package com.github.funczz.ruby_calc.android.destination.program

import com.github.funczz.ruby_calc.android.TextFieldState
import java.time.Instant

data class ProgramEditUiState(
    val id: Long? = null,
    val name: TextFieldState = TextFieldState(text = ""),
    val description: TextFieldState = TextFieldState(text = ""),
    val hint: TextFieldState = TextFieldState(text = ""),
    val code: TextFieldState = TextFieldState(text = ""),
    val createAt: Instant? = null,
    val updatedAt: Instant? = null,
)
