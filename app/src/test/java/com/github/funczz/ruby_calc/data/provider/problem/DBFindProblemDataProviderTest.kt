package com.github.funczz.ruby_calc.data.provider.problem

import com.github.funczz.ruby_calc.core.data.provider.problem.FindProblemDataProvider
import com.github.funczz.ruby_calc.data.db.entity.problem
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.ktorm.entity.count
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
class DBFindProblemDataProviderTest {

    @Test
    fun find() {
        val programId = dbTestUtil.database.insertAndGenerateKeyProgram()
        repeat(5) {
            dbTestUtil.database.insertAndGenerateKeyProblem(
                programId = programId,
                name = "get greeting $it"
            )
        }
        repeat(5) {
            dbTestUtil.database.insertAndGenerateKeyProblem(
                programId = programId,
                name = "GET GREETING $it"
            )
        }
        assertEquals(10, dbTestUtil.database.problem.count())

        val actual = provider.find(value = "GET GREETING %")
        assertEquals(5, actual.size)
    }

    @Test
    fun `find - programId`() {
        val programId1 = dbTestUtil.database.insertAndGenerateKeyProgram(name = "greeting1")
        repeat(1) {
            dbTestUtil.database.insertAndGenerateKeyProblem(
                programId = programId1,
                name = "get greeting $it"
            )
        }
        val programId2 = dbTestUtil.database.insertAndGenerateKeyProgram(name = "greeting2")
        repeat(2) {
            dbTestUtil.database.insertAndGenerateKeyProblem(
                programId = programId2,
                name = "GET GREETING $it"
            )
        }
        assertEquals(3, dbTestUtil.database.problem.count())

        assertEquals(1, provider.find(value = "%", programId = programId1).size)
        assertEquals(0, provider.find(value = "GET%", programId = programId1).size)
        assertEquals(2, provider.find(value = "%", programId = programId2).size)
        assertEquals(0, provider.find(value = "get%", programId = programId2).size)
    }

    private val dbTestUtil = DBTestUtil()

    private lateinit var provider: FindProblemDataProvider

    @Before
    fun beforeEach() {
        dbTestUtil.setUp()
        provider = DBFindProblemDataProvider(database = dbTestUtil.database)
    }

    @After
    fun afterEach() {
        dbTestUtil.tearDown()
    }

}
