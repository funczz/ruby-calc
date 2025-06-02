package com.github.funczz.ruby_calc.data.provider.answer

import com.github.funczz.ruby_calc.core.data.provider.answer.FindAnswerDataProvider
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
class DBFindAnswerDataProviderTest {

    @Test
    fun find() {
        val programId = dbTestUtil.database.insertAndGenerateKeyProgram()
        val problemId = dbTestUtil.database.insertAndGenerateKeyProblem(programId = programId)
        repeat(5) {
            dbTestUtil.database.insertAndGenerateKeyAnswer(
                problemId = problemId,
                value = "$it, hello world."
            )
        }
        repeat(5) {
            dbTestUtil.database.insertAndGenerateKeyAnswer(
                problemId = problemId,
                value = "$it, HELLO WORLD!"
            )
        }
        assertEquals(10, dbTestUtil.database.answer.count())

        val actual = provider.find(problemId = problemId, value = "%, hello world.")
        assertEquals(5, actual.size)
    }

    private val dbTestUtil = DBTestUtil()

    private lateinit var provider: FindAnswerDataProvider

    @Before
    fun beforeEach() {
        dbTestUtil.setUp()
        provider = DBFindAnswerDataProvider(database = dbTestUtil.database)
    }

    @After
    fun afterEach() {
        dbTestUtil.tearDown()
    }

}
