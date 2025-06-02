package com.github.funczz.ruby_calc.data.db

import com.github.funczz.kotlin.migration.model.Module
import com.github.funczz.ruby_calc.data.db.RubyCalcDBResources.Companion.DATABASE_NAME
import com.github.funczz.ruby_calc.data.db.RubyCalcDBResources.Companion.DATABASE_VERSIONS
import java.io.File

@Suppress("Unused", "MemberVisibilityCanBePrivate")
class BackupDBResources(

    private val filesDirectory: File,

    propertiesPath: String = "db/dbcp.properties",

    ) : DBResources(

    module = Module(DATABASE_NAME, DATABASE_VERSIONS),

    dataSource = run {
        DataSourceFactory.newDataSource(propertiesPath = propertiesPath) {
            var url = it.getProperty("url")
            url = url.replace("%FILES_DIR%", filesDirectory.canonicalPath)
            url = url.replace("%DB_DIR%", DATABASE_DIRECTORY)
            url = url.replace("%DB_NAME%", DATABASE_NAME)
            it.setProperty("url", url)
        }
    }
) {

    companion object {
        const val DATABASE_DIRECTORY = "backup-db"

        fun getDatabaseDirectoryFile(filesDirectory: File): File {
            return File(filesDirectory.canonicalFile, DATABASE_DIRECTORY)
        }

    }
}
