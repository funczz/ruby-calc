package com.github.funczz.ruby_calc.data.db

import com.github.funczz.kotlin.migration.model.Module
import com.github.funczz.kotlin.migration.model.Version
import com.github.funczz.kotlin.migration.model.patch.sql.LoggingSQLPatch
import com.github.funczz.kotlin.migration.model.patch.sql.SQLPatch
import java.io.File

@Suppress("Unused", "MemberVisibilityCanBePrivate")
class RubyCalcDBResources(

    filesDirectory: File,

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

        const val DATABASE_DIRECTORY = "db"

        const val DATABASE_NAME = "ruby-calc"

        val DATABASE_VERSIONS = listOf(
            Version(
                versionId = "1.0",
                LoggingSQLPatch(
                    patch = SQLPatch(
                        tag = "",
                        classLoader = Thread.currentThread().contextClassLoader as ClassLoader,
                        up = "db/migration/v1_0/up/program_create_table_program.sql",
                        down = "db/migration/v1_0/down/program_drop_table_program.sql"
                    )
                ),
                LoggingSQLPatch(
                    patch = SQLPatch(
                        tag = "",
                        classLoader = Thread.currentThread().contextClassLoader as ClassLoader,
                        up = "db/migration/v1_0/up/problem_create_table_problem.sql",
                        down = "db/migration/v1_0/down/problem_drop_table_problem.sql"
                    )
                ),
                LoggingSQLPatch(
                    patch = SQLPatch(
                        tag = "",
                        classLoader = Thread.currentThread().contextClassLoader as ClassLoader,
                        up = "db/migration/v1_0/up/problem_element_create_table_problem_element.sql",
                        down = "db/migration/v1_0/down/problem_element_drop_table_problem_element.sql"
                    )
                ),
                LoggingSQLPatch(
                    patch = SQLPatch(
                        tag = "",
                        classLoader = Thread.currentThread().contextClassLoader as ClassLoader,
                        up = "db/migration/v1_0/up/problem_answer_create_table_problem_answer.sql",
                        down = "db/migration/v1_0/down/problem_answer_drop_table_problem_answer.sql"
                    )
                ),
            ),
            Version(
                versionId = "1.1",
                LoggingSQLPatch(
                    patch = SQLPatch(
                        tag = "",
                        classLoader = Thread.currentThread().contextClassLoader as ClassLoader,
                        up = "db/migration/v1_1/up/setting_create_table_setting.sql",
                        down = "db/migration/v1_1/down/setting_drop_table_setting.sql"
                    )
                ),
            ),
        )

        fun getDatabaseDirectoryFile(filesDirectory: File): File {
            return File(filesDirectory.canonicalFile, DATABASE_DIRECTORY)
        }

    }

}
