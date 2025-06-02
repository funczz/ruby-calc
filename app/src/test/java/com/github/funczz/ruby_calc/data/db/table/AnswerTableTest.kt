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
import util.DBTestUtil.Companion.insertAndGenerateKeyProblem
import util.DBTestUtil.Companion.insertAndGenerateKeyProgram
import java.io.File
import java.sql.Timestamp
import java.util.concurrent.TimeUnit


@RunWith(RobolectricTestRunner::class)
@Config(
    manifest = "src/main/AndroidManifest.xml",
    packageName = "com.github.funczz.ruby_calc.android"
)
class AnswerTableTest {

    @Test
    fun insert() {
        val programId = database.useTransaction {
            database.insertAndGenerateKeyProgram()
        }
        val problemId = database.useTransaction {
            database.insertAndGenerateKeyProblem(programId = programId)
        }
        val actual = database.useTransaction {
            database.insert(AnswerTable) {
                set(it.problemId, problemId)
                set(it.value, "hello world.")
            }
        }
        assertEquals(1, actual)
        assertEquals(1, database.sequenceOf(AnswerTable).count())
        for (row in database.from(AnswerTable).select()) {
            assertTrue(row[AnswerTable.id]!!.toInt() >= 0)
            assertEquals(problemId, row[AnswerTable.problemId])
            assertEquals("hello world.", row[AnswerTable.value])
            assertTrue(0L < (row[AnswerTable.createdAt] as Timestamp).time)
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
        val problemId = database.useTransaction {
            database.insertAndGenerateKey(ProblemTable) {
                set(it.name, "get greeting")
                set(it.programId, programId)
                set(it.comment, "-")
            } as Long
        }

        val actual = database.useTransaction {
            database.insertAndGenerateKey(AnswerTable) {
                set(it.problemId, problemId)
                set(it.value, "hello world.")
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
