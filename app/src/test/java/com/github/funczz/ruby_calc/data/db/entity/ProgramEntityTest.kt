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
import java.util.concurrent.TimeUnit

@RunWith(RobolectricTestRunner::class)
@Config(
    manifest = "src/main/AndroidManifest.xml",
    packageName = "com.github.funczz.ruby_calc.android"
)
class ProgramEntityTest {

    @Test
    fun count() {
        assertEquals(0, database.program.count())
    }

    @Test
    fun add() {
        database.useTransaction {
            try {
                //add
                database.program.add(
                    entity = ProgramEntity {
                        name = "greeting"
                        description = "greeting(EN)"
                        hint = "-"
                        code = "hello world."
                    }
                )
                it.commit()
            } catch (th: Throwable) {
                it.rollback()
            }
        }

        assertEquals(1, database.program.count())
    }

    @Test
    fun update() {
        database.useTransaction {
            try {
                database.program.add(
                    entity = ProgramEntity {
                        name = "greeting"
                        description = "greeting(EN)"
                        hint = "-"
                        code = "hello world."
                    }
                )
                //update
                TimeUnit.MICROSECONDS.sleep(5L)
                database.program.find { e -> e.name eq "greeting" }.also { e ->
                    e!!.name = "GREETING"
                    database.program.update(e)
                }
                it.commit()
            } catch (th: Throwable) {
                it.rollback()
            }
        }

        assertEquals(1, database.program.filter { e -> e.name eq "GREETING" }.count())
        val actual = database.program.find { e -> e.name eq "GREETING" }!!
        assertEquals("GREETING", actual.name)
    }

    @Test
    fun flushChanges() {
        database.useTransaction {
            try {
                database.program.add(
                    entity = ProgramEntity {
                        name = "greeting"
                        description = "greeting(EN)"
                        hint = "-"
                        code = "hello world."
                    }
                )
                database.program.find { e -> e.name eq "greeting" }.also { e ->
                    e!!.name = "GREETING"
                }!!.flushChanges()
                it.commit()
            } catch (th: Throwable) {
                it.rollback()
            }
        }

        assertEquals(1, database.program.filter { e -> e.name eq "GREETING" }.count())
    }

    @Test
    fun delete() {
        database.useTransaction {
            try {
                database.program.add(
                    entity = ProgramEntity {
                        name = "greeting"
                        description = "greeting(EN)"
                        hint = "-"
                        code = "hello world."
                    }
                )
                it.commit()
            } catch (th: Throwable) {
                it.rollback()
            }
        }
        assertEquals(1, database.program.filter { e -> e.name eq "greeting" }.count())

        database.useTransaction {
            database.program.find { e -> e.name eq "greeting" }!!.delete()
            it.commit()
        }

        assertEquals(0, database.program.filter { e -> e.name eq "greeting" }.count())
    }

    @Test
    fun removeIf() {
        database.useTransaction {
            try {
                database.program.add(
                    entity = ProgramEntity {
                        name = "greeting"
                        description = "greeting(EN)"
                        hint = "-"
                        code = "hello world."
                    }
                )
                it.commit()
            } catch (th: Throwable) {
                it.rollback()
            }
        }
        assertEquals(1, database.program.filter { e -> e.name eq "greeting" }.count())

        database.useTransaction {
            database.program.removeIf { e -> e.name eq "greeting" }
            it.commit()
        }

        assertEquals(0, database.program.filter { e -> e.name eq "greeting" }.count())
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
