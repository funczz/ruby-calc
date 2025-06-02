package com.github.funczz.ruby_calc.data.db.table

import com.github.funczz.ruby_calc.data.db.RubyCalcDBResources
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.ktorm.database.Database
import org.ktorm.dsl.from
import org.ktorm.dsl.insert
import org.ktorm.dsl.insertAndGenerateKey
import org.ktorm.dsl.select
import org.ktorm.entity.count
import org.ktorm.entity.sequenceOf
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.File
import java.sql.Timestamp
import java.util.concurrent.TimeUnit

@RunWith(RobolectricTestRunner::class)
@Config(
    manifest = "src/main/AndroidManifest.xml",
    packageName = "com.github.funczz.ruby_calc.android"
)
class ProgramTableTest {

    @Test
    fun insert() {
        val result: Int
        database.useTransaction {
            result = database.insert(ProgramTable) {
                set(it.name, "greeting")
                set(it.description, "greeting(EN)")
                set(it.hint, "-")
                set(it.code, "hello world.")
            }
        }
        assertEquals(1, result)
        assertEquals(1, database.sequenceOf(ProgramTable).count())
        for (row in database.from(ProgramTable).select()) {
            assertTrue(row[ProgramTable.id]!!.toInt() >= 0)
            assertEquals("greeting", row[ProgramTable.name])
            assertEquals("greeting(EN)", row[ProgramTable.description])
            assertEquals("-", row[ProgramTable.hint])
            assertEquals("hello world.", row[ProgramTable.code])
            assertTrue(0L < (row[ProgramTable.createdAt] as Timestamp).time)
            assertTrue(0L < (row[ProgramTable.updatedAt] as Timestamp).time)
        }
    }

    @Test
    fun insertAndGenerateKey() {
        val result: Long
        database.useTransaction {
            result = database.insertAndGenerateKey(ProgramTable) {
                set(it.name, "greeting")
                set(it.description, "greeting(EN)")
                set(it.hint, "-")
                set(it.code, "hello world.")
            } as Long
        }

        assertTrue(0L <= result)
    }

    private val filesDirectory = File(".", "build")

    private val dbDir = File(filesDirectory, RubyCalcDBResources.DATABASE_DIRECTORY)

    private lateinit var dbResources: RubyCalcDBResources

    private lateinit var database: Database

    @Before
    fun beforeEach() {
        if (dbDir.exists()) dbDir.deleteRecursively()
        TimeUnit.MICROSECONDS.sleep(100L)
        assertTrue(!dbDir.exists())

        dbResources = RubyCalcDBResources(
            filesDirectory = filesDirectory,
        )
        dbResources.setupDB()
        database = dbResources.database
    }

    @After
    fun afterEach() {
        dbResources.shutdownDB()
        TimeUnit.MICROSECONDS.sleep(100L)
    }

}
