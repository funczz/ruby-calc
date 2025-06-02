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
import util.DBTestUtil.Companion.insertAndGenerateKeyProgram

@RunWith(RobolectricTestRunner::class)
@Config(
    manifest = "src/main/AndroidManifest.xml",
    packageName = "com.github.funczz.ruby_calc.android"
)
class ProblemEntityTest {

    @Test
    fun add() {
        database.useTransaction {
            try {
                val newProgramId = database.insertAndGenerateKeyProgram()

                //add
                database.problem.add(
                    entity = ProblemEntity {
                        name = "get greeting"
                        programId = newProgramId
                        comment = "-"
                    }
                )
                it.commit()
            } catch (th: Throwable) {
                it.rollback()
            }
        }

        assertEquals(1, database.problem.count())
    }

    @Test
    fun update() {
        database.useTransaction {
            try {
                val newProgramId = database.insertAndGenerateKeyProgram()

                database.problem.add(
                    entity = ProblemEntity {
                        name = "get greeting"
                        programId = newProgramId
                        comment = "-"
                    }
                )
                //update
                database.problem.find { e -> e.name eq "get greeting" }.also { e ->
                    e!!.name = "GET GREETING"
                    database.problem.update(e)
                }
                it.commit()
            } catch (th: Throwable) {
                it.rollback()
            }
        }

        assertEquals(1, database.problem.filter { e -> e.name eq "GET GREETING" }.count())
    }

    @Test
    fun flushChanges() {
        database.useTransaction {
            try {
                val newProgramId = database.insertAndGenerateKeyProgram()

                database.problem.add(
                    entity = ProblemEntity {
                        name = "get greeting"
                        programId = newProgramId
                        comment = "-"
                    }
                )
                //flushChanges
                database.problem.find { e -> e.name eq "get greeting" }.also { e ->
                    e!!.name = "GET GREETING"
                }!!.flushChanges()
                it.commit()
            } catch (th: Throwable) {
                it.rollback()
            }
        }

        assertEquals(1, database.problem.filter { e -> e.name eq "GET GREETING" }.count())
    }

    @Test
    fun delete() {
        database.useTransaction {
            try {
                val newProgramId = database.insertAndGenerateKeyProgram()

                database.problem.add(
                    entity = ProblemEntity {
                        name = "get greeting"
                        programId = newProgramId
                        comment = "-"
                    }
                )
                it.commit()
            } catch (th: Throwable) {
                it.rollback()
            }
        }
        assertEquals(1, database.problem.count())

        //delete
        database.useTransaction {
            database.problem.find { e -> e.name eq "get greeting" }!!.delete()
            it.commit()
        }

        assertEquals(0, database.problem.count())
    }


    @Test
    fun removeIf() {
        database.useTransaction {
            try {
                val newProgramId = database.insertAndGenerateKeyProgram()

                database.problem.add(
                    entity = ProblemEntity {
                        name = "get greeting"
                        programId = newProgramId
                        comment = "-"
                    }
                )
                it.commit()
            } catch (th: Throwable) {
                it.rollback()
            }
        }
        assertEquals(1, database.problem.count())

        //removeIf
        database.useTransaction {
            database.problem.removeIf { e -> e.name eq "get greeting" }
            it.commit()
        }

        assertEquals(0, database.problem.count())
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
