package com.github.funczz.ruby_calc.data.provider

import com.github.funczz.ruby_calc.core.data.provider.BackupDataProvider
import com.github.funczz.ruby_calc.data.db.BackupDBResources
import com.github.funczz.ruby_calc.data.db.RubyCalcDBResources
import com.github.funczz.ruby_calc.data.db.util.DatabaseExt
import org.zeroturnaround.zip.ZipUtil
import java.io.File
import java.io.InputStream
import java.io.OutputStream

class DBBackupDataProvider(

    private val rubyCalcDBResources: RubyCalcDBResources

) : BackupDataProvider, DatabaseExt {

    override fun backup(filesDirectory: File, backup: File) {
        backupPerformed(filesDirectory = filesDirectory) {
            ZipUtil.pack(it, backup)
        }
    }

    override fun backup(filesDirectory: File, backup: OutputStream) {
        backupPerformed(filesDirectory = filesDirectory) {
            ZipUtil.pack(it, backup)
        }
    }

    override fun restore(filesDirectory: File, backup: File) {
        restorePerformed(filesDirectory = filesDirectory) {
            ZipUtil.unpack(backup, it)
        }
    }

    override fun restore(filesDirectory: File, backup: InputStream) {
        restorePerformed(filesDirectory = filesDirectory) {
            ZipUtil.unpack(backup, it)
        }
    }

    private fun backupPerformed(filesDirectory: File, pack: (File) -> Unit) {
        val currentVersionId = rubyCalcDBResources.migration.getCurrentVersionId()
        if (currentVersionId == "") {
            throw IllegalStateException("Database is empty.")
        }
        val backupDatabaseDirectory = BackupDBResources.getDatabaseDirectoryFile(
            filesDirectory = filesDirectory
        )
        if (backupDatabaseDirectory.exists()) backupDatabaseDirectory.deleteRecursively()
        val backupDBResources = BackupDBResources(filesDirectory = filesDirectory)
        backupDBResources.migration.initialize()
        backupDBResources.migration.migrate(
            versionId = currentVersionId,
            tags = arrayOf(rubyCalcDBResources.tag)
        )
        rubyCalcDBResources.database.clone(cloneDatabase = backupDBResources.database)
        backupDBResources.shutdownDB()
        pack(backupDatabaseDirectory)
        if (backupDatabaseDirectory.exists()) backupDatabaseDirectory.deleteRecursively()
    }

    private fun restorePerformed(filesDirectory: File, unpack: (File) -> Unit) {
        val backupDatabaseDirectory = BackupDBResources.getDatabaseDirectoryFile(
            filesDirectory = filesDirectory
        )
        if (backupDatabaseDirectory.exists()) backupDatabaseDirectory.deleteRecursively()
        unpack(backupDatabaseDirectory)
        val backupDBResources = BackupDBResources(filesDirectory = filesDirectory)
        val currentVersionId = backupDBResources.migration.getCurrentVersionId()
        if (currentVersionId == "") {
            throw IllegalStateException("Backup Database is empty.")
        }
        rubyCalcDBResources.rollbackAll()
        rubyCalcDBResources.migration.initialize()
        rubyCalcDBResources.migration.migrate(versionId = currentVersionId)
        backupDBResources.database.clone(cloneDatabase = rubyCalcDBResources.database)
        rubyCalcDBResources.migration.migrate()
        backupDBResources.shutdownDB()
        if (backupDatabaseDirectory.exists()) backupDatabaseDirectory.deleteRecursively()
    }
}
