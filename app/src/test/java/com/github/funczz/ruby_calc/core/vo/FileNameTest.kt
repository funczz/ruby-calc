package com.github.funczz.ruby_calc.core.vo

import org.junit.Assert.assertTrue
import org.junit.Test

class FileNameTest {

    @Test
    fun defaultBackupFile() {
        val actual = FileName.defaultBackupFileName
        assertTrue(actual.matches("""RubyCalc-Backup-\d{8}-\d{6}\.zip""".toRegex()))
    }

}
