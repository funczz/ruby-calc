package com.github.funczz.ruby_calc.data.provider.problem

import com.github.funczz.ruby_calc.core.data.provider.problem.CRUDProblemDataProvider
import com.github.funczz.ruby_calc.data.db.entity.problem
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
import util.DBTestUtil.Companion.insertAndGenerateKeyProblem
import util.DBTestUtil.Companion.insertAndGenerateKeyProgram
import kotlin.jvm.optionals.getOrNull

@RunWith(RobolectricTestRunner::class)
@Config(
    manifest = "src/main/AndroidManifest.xml",
    packageName = "com.github.funczz.ruby_calc.android"
)
class DBCRUDProblemDataProviderTest {

    @Test
    fun count() {
        assertEquals(0, provider.count())
    }

    @Test
    fun create() {
        assertEquals(0, dbTestUtil.database.problem.count())
        val programId = dbTestUtil.database.insertAndGenerateKeyProgram()
        //create
        provider.create(
            name = "get greeting",
            programId = programId,
            comment = "-",
        )
        assertEquals(1, dbTestUtil.database.problem.count())
    }

    @Test
    fun `read by id`() {
        val expected = "get greeting"
        val programId = dbTestUtil.database.insertAndGenerateKeyProgram()
        val id = dbTestUtil.database.insertAndGenerateKeyProblem(
            programId = programId,
            name = expected,
        )
        //read
        val actual = provider.read(id = id)
        assertEquals(expected, actual.get().name)
    }

    @Test
    fun `read by name`() {
        val expected = "get greeting"
        val programId = dbTestUtil.database.insertAndGenerateKeyProgram()
        dbTestUtil.database.insertAndGenerateKeyProblem(
            programId = programId,
            name = expected,
        )
        //read
        val actual = provider.read(name = expected)
        assertEquals(expected, actual.get().name)
    }

    @Test
    fun update() {
        val expected = "GET GREETING"
        val programId = dbTestUtil.database.insertAndGenerateKeyProgram()
        val id = dbTestUtil.database.insertAndGenerateKeyProblem(programId = programId)
        //update
        provider.update(id = id, name = expected)
        val actual = provider.read(id = id)
        actual.getOrNull()?.let { println(it) }
        assertEquals(expected, actual.get().name)
        assertNotEquals(actual.get().createdAt.nano, actual.get().updatedAt.nano)
    }

    @Test
    fun delete() {
        val programId = dbTestUtil.database.insertAndGenerateKeyProgram()
        val id = dbTestUtil.database.insertAndGenerateKeyProblem(programId = programId)
        assertEquals(1, dbTestUtil.database.problem.count())
        //delete
        provider.delete(id = id)
        assertEquals(0, dbTestUtil.database.problem.count())
    }

    private val dbTestUtil = DBTestUtil()

    private lateinit var provider: CRUDProblemDataProvider

    @Before
    fun beforeEach() {
        dbTestUtil.setUp()
        provider = DBCRUDProblemDataProvider(database = dbTestUtil.database)
    }

    @After
    fun afterEach() {
        dbTestUtil.tearDown()
    }

}
