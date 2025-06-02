package com.github.funczz.ruby_calc.data.db.entity

import com.github.funczz.ruby_calc.data.db.table.ProgramTable
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.ktorm.database.Database
import org.ktorm.dsl.insertAndGenerateKey
import org.ktorm.entity.add
import org.ktorm.entity.count
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import util.DBTestUtil
import util.DBTestUtil.Companion.insertAndGenerateKeyProblem

@RunWith(RobolectricTestRunner::class)
@Config(
    manifest = "src/main/AndroidManifest.xml",
    packageName = "com.github.funczz.ruby_calc.android"
)
class ElementEntityTest {

    @Test
    fun add() {
        database.useTransaction {
            try {
                val newProgramId = database.insertAndGenerateKeyProgram()

                val newProblemId = database.insertAndGenerateKeyProblem(programId = newProgramId)

                //add
                database.element.add(
                    entity = ElementEntity {
                        problemId = newProblemId
                        elementIndex = 0
                        elementValue = "-"
                    }
                )
                it.commit()
            } catch (th: Throwable) {
                it.rollback()
            }
        }

        assertEquals(1, database.element.count())
    }

    private fun Database.insertAndGenerateKeyProgram(name: String = "greeting"): Long {
        return this.useTransaction {
            this.insertAndGenerateKey(ProgramTable) {
                set(it.name, name)
                set(it.description, "greeting(EN)")
                set(it.hint, "-")
                set(it.code, "'hello world.'")
            } as Long
        }
    }

    private val dbTestUtil = DBTestUtil()

    private lateinit var database: Database

    @Before
    fun beforeEach() {
        dbTestUtil.setUp()
        database = dbTestUtil.database
    }

    @After
    fun afterEach() {
        dbTestUtil.tearDown()
    }

}
