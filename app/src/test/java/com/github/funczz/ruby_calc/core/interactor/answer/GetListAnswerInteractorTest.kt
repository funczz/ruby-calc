package com.github.funczz.ruby_calc.core.interactor.answer

import com.github.funczz.ruby_calc.core.usecase.answer.GetListAnswerUseCase
import com.github.funczz.ruby_calc.core.vo.OrderBy
import com.github.funczz.ruby_calc.data.provider.answer.DBFindAnswerDataProvider
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import util.DBTestUtil
import util.DBTestUtil.Companion.insertAndGenerateKeyAnswer
import util.DBTestUtil.Companion.insertAndGenerateKeyProblem
import util.DBTestUtil.Companion.insertAndGenerateKeyProgram

@Suppress("NonAsciiCharacters")
@RunWith(RobolectricTestRunner::class)
@Config(
    manifest = "src/main/AndroidManifest.xml",
    packageName = "com.github.funczz.ruby_calc.android"
)
class GetListAnswerInteractorTest {

    @Test
    fun `invoke - value はデフォルト値, データ は0件`() {
        val expected = 0

        val inputData = GetListAnswerUseCase.InputData(problemId = 0L)
        val actual = useCase(inputData = inputData)
        println(actual)
        assertEquals(expected, actual.size)
    }

    @Test
    fun `invoke - value はデフォルト値, データ は1件`() {
        val expected = 1

        val programId = dbTestUtil.database.insertAndGenerateKeyProgram()
        val problemId = dbTestUtil.database.insertAndGenerateKeyProblem(programId = programId)
        dbTestUtil.database.insertAndGenerateKeyAnswer(problemId = problemId)

        val inputData = GetListAnswerUseCase.InputData(problemId = problemId)
        val actual = useCase(inputData = inputData)
        println(actual)
        assertEquals(expected, actual.size)
    }

    @Test
    fun `invoke - value は'hello _', データ は1件`() {
        val expected = 1

        val programId = dbTestUtil.database.insertAndGenerateKeyProgram()
        val problemId = dbTestUtil.database.insertAndGenerateKeyProblem(programId = programId)
        dbTestUtil.database.insertAndGenerateKeyAnswer(problemId = problemId)

        val inputData = GetListAnswerUseCase.InputData(
            problemId = problemId,
            value = "hello %",
        )
        val actual = useCase(inputData = inputData)
        println(actual)
        assertEquals(expected, actual.size)
    }

    @Test
    fun `invoke - limit は5, データ は10件`() {
        val expected = 5

        val programId = dbTestUtil.database.insertAndGenerateKeyProgram()
        val problemId = dbTestUtil.database.insertAndGenerateKeyProblem(programId = programId)
        repeat(10) {
            dbTestUtil.database.insertAndGenerateKeyAnswer(
                problemId = problemId,
                value = "$it, hello world."
            )
        }
        val inputData = GetListAnswerUseCase.InputData(
            problemId = problemId,
            limit = expected
        )
        val actual = useCase(inputData = inputData)
        println(actual)
        assertEquals(expected, actual.size)
    }

    @Test
    fun `invoke - OrderBy はASC, データ は10件`() {
        val expected = "0, hello world."

        val programId = dbTestUtil.database.insertAndGenerateKeyProgram()
        val problemId = dbTestUtil.database.insertAndGenerateKeyProblem(programId = programId)
        repeat(10) {
            dbTestUtil.database.insertAndGenerateKeyAnswer(
                problemId = problemId,
                value = "$it, hello world."
            )
        }

        val inputData = GetListAnswerUseCase.InputData(
            problemId = problemId,
            orderBy = OrderBy.ASC
        )
        val actual = useCase(inputData = inputData)
        println(actual)
        assertEquals(expected, actual.first().value)
    }

    @Test
    fun `invoke - OrderBy はDESC, データ は10件`() {
        val expected = "9, hello world."

        val programId = dbTestUtil.database.insertAndGenerateKeyProgram()
        val problemId = dbTestUtil.database.insertAndGenerateKeyProblem(programId = programId)
        repeat(10) {
            dbTestUtil.database.insertAndGenerateKeyAnswer(
                problemId = problemId,
                value = "$it, hello world."
            )
        }

        val inputData = GetListAnswerUseCase.InputData(
            problemId = problemId,
            orderBy = OrderBy.DESC
        )
        val actual = useCase(inputData = inputData)
        println(actual)
        assertEquals(expected, actual.first().value)
    }

    private val dbTestUtil = DBTestUtil()

    private lateinit var useCase: GetListAnswerUseCase

    @Before
    fun beforeEach() {
        dbTestUtil.setUp()
        useCase = GetListAnswerInteractor(
            provider = DBFindAnswerDataProvider(database = dbTestUtil.database)
        )
    }

    @After
    fun afterEach() {
        dbTestUtil.tearDown()
    }

}
