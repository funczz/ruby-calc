package com.github.funczz.ruby_calc.core.model.program

import java.time.Instant

data class ProgramModel(

    val id: Long,

    val name: String,

    val description: String,

    val hint: String,

    val code: String,

    val createdAt: Instant,

    val updatedAt: Instant,

    )
