package com.github.funczz.ruby_calc.data.db

import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.File
import java.util.concurrent.TimeUnit

@RunWith(RobolectricTestRunner::class)
@Config(
    manifest = "src/main/AndroidManifest.xml",
    packageName = "com.github.funczz.ruby_calc.android"
)
class RubyCalcDBResourcesRollbackTest {

    @Test
    fun `rollbackAll - initDB`() {
        dbResources.migration.initialize()
        dbResources.rollbackAll()
        TimeUnit.MICROSECONDS.sleep(100L)

        assertEquals("", dbResources.migration.getCurrentVersionId())
    }

    @Test
    fun rollbackAll() {
        dbResources.setupDB()
        dbResources.rollbackAll()
        TimeUnit.MICROSECONDS.sleep(100L)

        assertEquals("", dbResources.migration.getCurrentVersionId())
    }

    private lateinit var dbResources: RubyCalcDBResources

    @Before
    fun beforeEach() {
        if (dbDir.exists()) dbDir.deleteRecursively()
        TimeUnit.MICROSECONDS.sleep(100L)
        assertTrue(!dbDir.exists())

        dbResources = RubyCalcDBResources(
            filesDirectory = filesDirectory,
        )
    }

    @After
    fun afterEach() {
        dbResources.shutdownDB()
        TimeUnit.MICROSECONDS.sleep(100L)
    }

    companion object {

        private val filesDirectory = File(".", "build")

        private val dbDir = File(filesDirectory, RubyCalcDBResources.DATABASE_DIRECTORY)

        @JvmStatic
        @BeforeClass
        fun beforeAll() {
            if (dbDir.exists()) dbDir.deleteRecursively()
            TimeUnit.MICROSECONDS.sleep(100L)

            assertTrue(!dbDir.exists())
        }
    }
}
