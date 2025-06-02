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
import util.DBTestUtil
import java.io.File
import java.util.concurrent.TimeUnit

@RunWith(RobolectricTestRunner::class)
@Config(
    manifest = "src/main/AndroidManifest.xml",
    packageName = "com.github.funczz.ruby_calc.android"
)
class RubyCalcDBResourcesTest {

    @Test
    fun migrate() {
        assertEquals(Unit, dbResources.setupDB())
    }

    private lateinit var dbResources: RubyCalcDBResources

    @Before
    fun beforeEach() {
        dbResources = RubyCalcDBResources(
            filesDirectory = filesDirectory.canonicalFile,
        )
    }

    @After
    fun afterEach() {
        dbResources.shutdownDB()
    }

    companion object {

        private val filesDirectory = DBTestUtil.filesDirectory

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
