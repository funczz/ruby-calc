package com.github.funczz.ruby_calc.core.model.program

data class ProgramEdit(
    val id: Long? = null,
    val name: String = "",
    val description: String = "",
    val hint: String = "",
    val code: String = "",
)
