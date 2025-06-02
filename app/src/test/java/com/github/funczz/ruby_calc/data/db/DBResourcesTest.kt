package com.github.funczz.ruby_calc.data.db

import com.github.funczz.kotlin.migration.model.Module
import com.github.funczz.kotlin.migration.model.Version
import com.github.funczz.kotlin.migration.model.patch.sql.SQLPatch
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import util.DBTestUtil
import java.io.File
import java.util.Properties
import java.util.concurrent.TimeUnit

class DBResourcesTest {

    @Test
    fun migrate() {
        assertEquals(Unit, dbResources.setupDB())
    }

    private lateinit var dbResources: DBResources

    @Before
    fun beforeEach() {
        val properties = Properties()
        properties.setProperty(
            "url", "jdbc:hsqldb:file:%s/%s/%s;shutdown=true".format(
                filesDirectory.canonicalPath,
                databaseDirectory,
                databaseName
            )
        )
        properties.setProperty("driverClassName", "org.hsqldb.jdbc.JDBCDriver")
        properties.setProperty("username", "sa")
        properties.setProperty("password", "sa")
        properties.setProperty("validationQuery", "select 1 from INFORMATION_SCHEMA.SYSTEM_USERS")
        properties.setProperty("defaultAutoCommit", "false")
        properties.setProperty("defaultTransactionIsolation", "8")
        dbResources = DBResources(
            dataSource = DataSourceFactory.newDataSource(properties = properties),
            module = Module(
                moduleId = "ruby-calc",
                Version(
                    versionId = "1.0.0",
                    SQLPatch(
                        classLoader = Thread.currentThread().contextClassLoader as ClassLoader,
                        up = "db/migration/v1_0/up/program_create_table_program.sql",
                        down = "db/migration/v1_0/down/program_drop_table_program.sql"
                    )
                )
            ),
        )
    }

    @After
    fun afterEach() {
        dbResources.shutdownDB()
    }

    companion object {

        private val filesDirectory = DBTestUtil.filesDirectory

        private val databaseDirectory = "%s".format("db")

        private val databaseName = "%s_db".format(DBResourcesTest::class.simpleName)

        private val dbDir = File(filesDirectory, databaseDirectory)

        @JvmStatic
        @BeforeClass
        fun beforeAll() {
            if (dbDir.exists()) dbDir.deleteRecursively()
            TimeUnit.MICROSECONDS.sleep(100L)

            assertTrue(!dbDir.exists())
        }
    }
}
