package com.github.funczz.ruby_calc.data.db.entity

import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.entity.add
import org.ktorm.entity.count
import org.ktorm.entity.filter
import org.ktorm.entity.find
import org.ktorm.entity.removeIf
import org.ktorm.entity.update
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import util.DBTestUtil
import util.DBTestUtil.Companion.insertAndGenerateKeyProblem
import util.DBTestUtil.Companion.insertAndGenerateKeyProgram

@RunWith(RobolectricTestRunner::class)
@Config(
    manifest = "src/main/AndroidManifest.xml",
    packageName = "com.github.funczz.ruby_calc.android"
)
class AnswerEntityTest {

    @Test
    fun add() {
        database.useTransaction {
            try {
                val newProgramId = database.insertAndGenerateKeyProgram()
                val newProblemId = database.insertAndGenerateKeyProblem(programId = newProgramId)

                //add
                database.answer.add(
                    entity = AnswerEntity {
                        problemId = newProblemId
                        value = "hello world."
                    }
                )
                it.commit()
            } catch (th: Throwable) {
                it.rollback()
            }
        }

        assertEquals(1, database.answer.count())
    }

    @Test
    fun update() {
        database.useTransaction {
            try {
                val newProgramId = database.insertAndGenerateKeyProgram()
                val newProblemId = database.insertAndGenerateKeyProblem(programId = newProgramId)
                database.answer.add(
                    entity = AnswerEntity {
                        problemId = newProblemId
                        value = "hello world."
                    }
                )

                //update
                database.answer.find { e -> e.problemId eq newProblemId }.also { e ->
                    e!!.value = "HELLO WORLD!"
                    database.answer.update(e)
                }
                it.commit()
            } catch (th: Throwable) {
                it.rollback()
            }
        }

        assertEquals(1, database.answer.filter { e -> e.value eq "HELLO WORLD!" }.count())
    }

    @Test
    fun flushChanges() {
        database.useTransaction {
            try {
                val newProgramId = database.insertAndGenerateKeyProgram()
                val newProblemId = database.insertAndGenerateKeyProblem(programId = newProgramId)
                database.answer.add(
                    entity = AnswerEntity {
                        problemId = newProblemId
                        value = "hello world."
                    }
                )
                //flushChanges
                database.answer.find { e -> e.problemId eq newProblemId }.also { e ->
                    e!!.value = "HELLO WORLD!"
                }!!.flushChanges()
                it.commit()
            } catch (th: Throwable) {
                it.rollback()
            }
        }

        assertEquals(1, database.answer.filter { e -> e.value eq "HELLO WORLD!" }.count())
    }

    @Test
    fun delete() {
        database.useTransaction {
            try {
                val newProgramId = database.insertAndGenerateKeyProgram()
                val newProblemId = database.insertAndGenerateKeyProblem(programId = newProgramId)
                database.answer.add(
                    entity = AnswerEntity {
                        problemId = newProblemId
                        value = "hello world."
                    }
                )
                it.commit()
            } catch (th: Throwable) {
                it.rollback()
            }
        }
        assertEquals(1, database.answer.count())

        //delete
        database.useTransaction {
            database.answer.find { e -> e.value eq "hello world." }!!.delete()
            it.commit()
        }

        assertEquals(0, database.answer.count())
    }


    @Test
    fun removeIf() {
        database.useTransaction {
            try {
                val newProgramId = database.insertAndGenerateKeyProgram()
                val newProblemId = database.insertAndGenerateKeyProblem(programId = newProgramId)
                database.answer.add(
                    entity = AnswerEntity {
                        problemId = newProblemId
                        value = "hello world."
                    }
                )
                it.commit()
            } catch (th: Throwable) {
                it.rollback()
            }
        }
        assertEquals(1, database.answer.count())

        //removeIf
        database.useTransaction {
            database.answer.removeIf { e -> e.value eq "hello world." }
            it.commit()
        }

        assertEquals(0, database.answer.count())
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
