package com.github.funczz.ruby_calc.data.db

import com.github.funczz.kotlin.migration.SQLMigration
import com.github.funczz.kotlin.migration.model.Module
import org.ktorm.database.Database
import javax.sql.DataSource

@Suppress("Unused", "MemberVisibilityCanBePrivate")
open class DBResources(

    val dataSource: DataSource,

    private val module: Module,

    ) {

    val tag: String by lazy {
        val url = dataSource.connection.metaData.url
        try {
            """^jdbc:(.*?):""".toRegex().find(url)?.groups?.get(1)?.value ?: ""
        } catch (ex: Exception) {
            ""
        }
    }

    val migration: SQLMigration by lazy {
        SQLMigration(
            module = module,
            dataSource = dataSource,
        )
    }

    val database: Database by lazy {
        Database.connect(dataSource)
    }

    fun setupDB() {
        migration.initialize()
        migration.migrate(tags = arrayOf(tag))
    }

    fun shutdownDB() {
        dataSource.connection.use { it.createStatement().executeUpdate("SHUTDOWN") }
    }

    fun rollbackAll() {
        while (migration.getCurrentVersionId() != "") {
            migration.rollback(tags = arrayOf(tag))
        }
    }

    init {
        //hsqldb - Direct vulnerabilities: CVE-2022-41853
        System.setProperty("hsqldb.method_class_names", "abc")
    }

}
