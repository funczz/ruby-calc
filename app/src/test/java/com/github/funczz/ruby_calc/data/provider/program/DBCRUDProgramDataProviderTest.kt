package com.github.funczz.ruby_calc.data.provider.program

import com.github.funczz.ruby_calc.data.db.entity.program
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.ktorm.entity.count
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import util.DBTestUtil
import util.DBTestUtil.Companion.insertAndGenerateKeyProgram
import kotlin.jvm.optionals.getOrNull

@RunWith(RobolectricTestRunner::class)
@Config(
    manifest = "src/main/AndroidManifest.xml",
    packageName = "com.github.funczz.ruby_calc.android"
)
class DBCRUDProgramDataProviderTest {

    @Test
    fun count() {
        assertEquals(0, provider.count())
    }

    @Test
    fun create() {
        assertEquals(0, dbTestUtil.database.program.count())
        //create
        provider.create(
            name = "greeting",
            description = "greeting(EN)",
            hint = "-",
            code = "hello world."
        )
        assertEquals(1, dbTestUtil.database.program.count())
    }

    @Test
    fun `read by id`() {
        val expected = "greeting"
        val id = dbTestUtil.database.insertAndGenerateKeyProgram(name = expected)
        //read
        val actual = provider.read(id = id)
        assertEquals(expected, actual.get().name)
    }

    @Test
    fun `read by name`() {
        val expected = "greeting"
        dbTestUtil.database.insertAndGenerateKeyProgram(name = expected)
        //read
        val actual = provider.read(name = expected)
        assertEquals(expected, actual.get().name)
    }

    @Test
    fun update() {
        val id = dbTestUtil.database.insertAndGenerateKeyProgram()
        //update
        provider.update(id = id, name = "GREETING")
        val actual = provider.read(id = id)
        actual.getOrNull()?.let { println(it) }
        assertEquals("GREETING", actual.get().name)
        assertNotEquals(actual.get().createdAt.nano, actual.get().updatedAt.nano)
    }

    @Test
    fun delete() {
        val id = dbTestUtil.database.insertAndGenerateKeyProgram()
        assertEquals(1, dbTestUtil.database.program.count())
        //delete
        provider.delete(id = id)
        assertEquals(0, dbTestUtil.database.program.count())
    }

    private val dbTestUtil = DBTestUtil()

    private lateinit var provider: DBCRUDProgramDataProvider

    @Before
    fun beforeEach() {
        dbTestUtil.setUp()
        provider = DBCRUDProgramDataProvider(database = dbTestUtil.database)
    }

    @After
    fun afterEach() {
        dbTestUtil.tearDown()
    }

}
