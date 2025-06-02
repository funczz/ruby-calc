package com.github.funczz.ruby_calc.core.data.provider.program

import com.github.funczz.ruby_calc.core.model.program.ProgramModel
import java.time.Instant
import java.util.Optional

interface CRUDProgramDataProvider {

    fun count(): Int

    fun create(name: String, description: String, hint: String, code: String): Int

    fun create(
        id: Long,
        name: String,
        description: String,
        hint: String,
        code: String,
        createdAt: Instant?,
        updatedAt: Instant?,
    ): Int

    fun read(id: Long): Optional<ProgramModel>

    fun read(name: String): Optional<ProgramModel>

    fun update(
        id: Long,
        name: String? = null,
        description: String? = null,
        hint: String? = null,
        code: String? = null
    ): Int

    fun delete(id: Long): Int

}
