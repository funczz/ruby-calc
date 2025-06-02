package com.github.funczz.ruby_calc.core.interactor.problem

import com.github.funczz.ruby_calc.core.usecase.problem.GetListProblemUseCase
import com.github.funczz.ruby_calc.core.vo.OrderBy
import com.github.funczz.ruby_calc.data.provider.problem.DBFindProblemDataProvider
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import util.DBTestUtil
import util.DBTestUtil.Companion.insertAndGenerateKeyProblem
import util.DBTestUtil.Companion.insertAndGenerateKeyProgram

@Suppress("NonAsciiCharacters")
@RunWith(RobolectricTestRunner::class)
@Config(
    manifest = "src/main/AndroidManifest.xml",
    packageName = "com.github.funczz.ruby_calc.android"
)
class GetListProblemInteractorTest {

    @Test
    fun `invoke - value はデフォルト値, データ は0件`() {
        val expected = 0

        val inputData = GetListProblemUseCase.InputData()
        val actual = useCase(inputData = inputData)
        println(actual)
        assertEquals(expected, actual.size)
    }

    @Test
    fun `invoke - value はデフォルト値, データ は1件`() {
        val expected = 1

        val programId = dbTestUtil.database.insertAndGenerateKeyProgram()
        dbTestUtil.database.insertAndGenerateKeyProblem(programId = programId)

        val inputData = GetListProblemUseCase.InputData()
        val actual = useCase(inputData = inputData)
        println(actual)
        assertEquals(expected, actual.size)
    }

    @Test
    fun `invoke - value は'get gr_', データ は1件`() {
        val expected = 1

        val programId = dbTestUtil.database.insertAndGenerateKeyProgram()
        dbTestUtil.database.insertAndGenerateKeyProblem(programId = programId)

        val inputData = GetListProblemUseCase.InputData(
            value = "get gr%"
        )
        val actual = useCase(inputData = inputData)
        println(actual)
        assertEquals(expected, actual.size)
    }

    @Test
    fun `invoke - programIdを指定, データ は1件`() {
        val expected = 1

        val programId = dbTestUtil.database.insertAndGenerateKeyProgram()
        dbTestUtil.database.insertAndGenerateKeyProblem(programId = programId)

        val inputData = GetListProblemUseCase.InputData(
            programId = programId
        )
        val actual = useCase(inputData = inputData)
        println(actual)
        assertEquals(expected, actual.size)
    }

    @Test
    fun `invoke - limit は5, データ は10件`() {
        val expected = 5

        val programId = dbTestUtil.database.insertAndGenerateKeyProgram()
        repeat(10) {
            dbTestUtil.database.insertAndGenerateKeyProblem(
                programId = programId,
                name = "get greeting-$it"
            )
        }
        val inputData = GetListProblemUseCase.InputData(
            limit = expected
        )
        val actual = useCase(inputData = inputData)
        println(actual)
        assertEquals(expected, actual.size)
    }

    @Test
    fun `invoke - OrderBy はASC, データ は10件`() {
        val expected = "get greeting-0"

        val programId = dbTestUtil.database.insertAndGenerateKeyProgram()
        repeat(10) {
            dbTestUtil.database.insertAndGenerateKeyProblem(
                programId = programId,
                name = "get greeting-$it"
            )
        }

        val inputData = GetListProblemUseCase.InputData(
            orderBy = OrderBy.ASC
        )
        val actual = useCase(inputData = inputData)
        println(actual)
        assertEquals(expected, actual.first().name)
    }

    @Test
    fun `invoke - OrderBy はDESC, データ は10件`() {
        val expected = "get greeting-9"

        val programId = dbTestUtil.database.insertAndGenerateKeyProgram()
        repeat(10) {
            dbTestUtil.database.insertAndGenerateKeyProblem(
                programId = programId,
                name = "get greeting-$it"
            )
        }

        val inputData = GetListProblemUseCase.InputData(
            orderBy = OrderBy.DESC
        )
        val actual = useCase(inputData = inputData)
        println(actual)
        assertEquals(expected, actual.first().name)
    }

    private val dbTestUtil = DBTestUtil()

    private lateinit var useCase: GetListProblemUseCase

    @Before
    fun beforeEach() {
        dbTestUtil.setUp()
        useCase = GetListProblemInteractor(
            provider = DBFindProblemDataProvider(database = dbTestUtil.database)
        )
    }

    @After
    fun afterEach() {
        dbTestUtil.tearDown()
    }

}
