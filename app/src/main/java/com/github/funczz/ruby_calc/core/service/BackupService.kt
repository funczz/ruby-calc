package com.github.funczz.ruby_calc.core.service

import com.github.funczz.ruby_calc.data.provider.DBBackupDataProvider
import java.io.File
import java.io.InputStream
import java.io.OutputStream

@Suppress("Unused", "MemberVisibilityCanBePrivate")
class BackupService(

    private val dbBackupDataProvider: DBBackupDataProvider,

    private val filesDirectory: File,

    ) {

    fun backup(backup: OutputStream) {
        dbBackupDataProvider.backup(filesDirectory = filesDirectory, backup = backup)
    }

    fun restore(backup: InputStream) {
        dbBackupDataProvider.restore(filesDirectory = filesDirectory, backup = backup)
    }

}
