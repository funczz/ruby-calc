package com.github.funczz.ruby_calc.data.provider.answer

import com.github.funczz.ruby_calc.core.data.provider.answer.CRUDAnswerDataProvider
import com.github.funczz.ruby_calc.data.db.entity.answer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.ktorm.entity.count
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import util.DBTestUtil
import util.DBTestUtil.Companion.insertAndGenerateKeyAnswer
import util.DBTestUtil.Companion.insertAndGenerateKeyProblem
import util.DBTestUtil.Companion.insertAndGenerateKeyProgram

@RunWith(RobolectricTestRunner::class)
@Config(
    manifest = "src/main/AndroidManifest.xml",
    packageName = "com.github.funczz.ruby_calc.android"
)
class DBCRUDAnswerDataProviderTest {

    @Test
    fun count() {
        val programId = dbTestUtil.database.insertAndGenerateKeyProgram()
        val problemId = dbTestUtil.database.insertAndGenerateKeyProblem(programId = programId)
        repeat(10) {
            dbTestUtil.database.insertAndGenerateKeyAnswer(
                problemId = problemId,
                value = "$it, hello world."
            )
        }
        assertEquals(10, provider.count(problemId = problemId))
    }

    @Test
    fun create() {
        assertEquals(0, dbTestUtil.database.answer.count())
        val programId = dbTestUtil.database.insertAndGenerateKeyProgram()
        val problemId = dbTestUtil.database.insertAndGenerateKeyProblem(programId = programId)
        //create
        provider.create(
            problemId = problemId,
            value = "hello world."
        )
        assertEquals(1, dbTestUtil.database.answer.count())
    }

    @Test
    fun read() {
        val expected = "hello world."
        val programId = dbTestUtil.database.insertAndGenerateKeyProgram()
        val problemId = dbTestUtil.database.insertAndGenerateKeyProblem(programId = programId)
        val answerId = dbTestUtil.database.insertAndGenerateKeyAnswer(
            problemId = problemId,
            value = expected
        )
        //read
        val actual = provider.read(id = answerId)
        assertEquals(expected, actual.get().value)
    }

    @Test
    fun delete() {
        val programId = dbTestUtil.database.insertAndGenerateKeyProgram()
        val problemId = dbTestUtil.database.insertAndGenerateKeyProblem(programId = programId)
        val answerId = dbTestUtil.database.insertAndGenerateKeyAnswer(problemId = problemId)
        assertEquals(1, dbTestUtil.database.answer.count())
        //delete
        provider.delete(id = answerId)
        assertEquals(0, dbTestUtil.database.answer.count())
    }

    private val dbTestUtil = DBTestUtil()

    private lateinit var provider: CRUDAnswerDataProvider

    @Before
    fun beforeEach() {
        dbTestUtil.setUp()
        provider = DBCRUDAnswerDataProvider(database = dbTestUtil.database)
    }

    @After
    fun afterEach() {
        dbTestUtil.tearDown()
    }

}
