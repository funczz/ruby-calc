package com.github.funczz.ruby_calc.core.data.provider

import java.io.File
import java.io.InputStream
import java.io.OutputStream

interface BackupDataProvider {

    fun backup(filesDirectory: File, backup: File)

    fun backup(filesDirectory: File, backup: OutputStream)

    fun restore(filesDirectory: File, backup: File)

    fun restore(filesDirectory: File, backup: InputStream)

}
