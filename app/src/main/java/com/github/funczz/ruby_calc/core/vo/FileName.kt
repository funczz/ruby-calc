package com.github.funczz.ruby_calc.core.vo

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

object FileName {

    val defaultBackupFileName: String
        get() = "RubyCalc-Backup-%s.zip".format(
            DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss").format(
                LocalDateTime.ofInstant(Instant.now(), ZoneOffset.systemDefault())
            )
        )

}
