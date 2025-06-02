package com.github.funczz.ruby_calc.data.db.table

import com.github.funczz.ruby_calc.data.db.RubyCalcDBResources
import com.github.funczz.ruby_calc.data.db.entity.problem
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.insert
import org.ktorm.dsl.insertAndGenerateKey
import org.ktorm.dsl.select
import org.ktorm.entity.count
import org.ktorm.entity.find
import org.ktorm.entity.sequenceOf
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.File
import java.util.concurrent.TimeUnit

@RunWith(RobolectricTestRunner::class)
@Config(
    manifest = "src/main/AndroidManifest.xml",
    packageName = "com.github.funczz.ruby_calc.android"
)
class ElementTableTest {

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
        database.useTransaction {
            database.insert(ProblemTable) {
                set(it.name, "get greeting")
                set(it.programId, programId)
                set(it.comment, "-")
            }
        }
        val problemId = database.problem.find { it.name eq "get greeting" }!!.id
        //insert
        val result = database.useTransaction {
            database.insert(ElementTable) {
                set(it.problemId, problemId)
                set(it.elementIndex, 0)
                set(it.elementValue, "-")
            }
        }
        assertEquals(1, result)
        assertEquals(1, database.sequenceOf(ElementTable).count())
        for (row in database.from(ElementTable).select()) {
            assertEquals(problemId, row[ElementTable.problemId])
            assertEquals(0, row[ElementTable.elementIndex])
            assertEquals("-", row[ElementTable.elementValue])
        }
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
