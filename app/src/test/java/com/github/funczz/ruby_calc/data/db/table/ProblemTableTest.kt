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
class ProblemTableTest {

    @Test
    fun insert() {
        val programId = database.useTransaction {
            database.insertAndGenerateKey(ProgramTable) {
                set(it.name, "greeting")
                set(it.description, "greeting(EN)")
                set(it.hint, "-")
                set(it.code, "'hello world.'")
            } as Long
        }
        val result: Int
        database.useTransaction {
            result = database.insert(ProblemTable) {
                set(it.name, "get greeting")
                set(it.programId, programId)
                set(it.comment, "-")
            }
        }
        assertEquals(1, result)
        assertEquals(1, database.sequenceOf(ProblemTable).count())
        for (row in database.from(ProblemTable).select()) {
            assertTrue(row[ProblemTable.id]!!.toInt() >= 0)
            assertEquals("get greeting", row[ProblemTable.name])
            assertEquals(programId, row[ProblemTable.programId])
            assertEquals("-", row[ProblemTable.comment])
            assertTrue(0L < (row[ProblemTable.createdAt] as Timestamp).time)
            assertTrue(0L < (row[ProblemTable.updatedAt] as Timestamp).time)
        }
    }

    @Test
    fun insertAndGenerateKey() {
        val programId = database.useTransaction {
            val id = database.insertAndGenerateKey(ProgramTable) { t ->
                set(t.name, "greeting")
                set(t.description, "greeting(EN)")
                set(t.hint, "-")
                set(t.code, "'hello world.'")
            } as Long
            it.commit()
            id
        }

        val actual = database.useTransaction {
            database.insertAndGenerateKey(ProblemTable) {
                set(it.name, "get greeting")
                set(it.programId, programId)
                set(it.comment, "-")
            } as Long
        }
        assertTrue(actual >= 0L)
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
