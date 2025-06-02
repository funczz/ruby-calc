package com.github.funczz.ruby_calc.core.interactor.program

import com.github.funczz.ruby_calc.core.usecase.program.GetListProgramUseCase
import com.github.funczz.ruby_calc.core.vo.OrderBy
import com.github.funczz.ruby_calc.data.provider.program.DBFindProgramDataProvider
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import util.DBTestUtil
import util.DBTestUtil.Companion.insertAndGenerateKeyProgram

@Suppress("NonAsciiCharacters")
@RunWith(RobolectricTestRunner::class)
@Config(
    manifest = "src/main/AndroidManifest.xml",
    packageName = "com.github.funczz.ruby_calc.android"
)
class GetListProgramInteractorTest {

    @Test
    fun `invoke - value はデフォルト値, データ は0件`() {
        val expected = 0

        val inputData = GetListProgramUseCase.InputData()
        val actual = useCase(inputData = inputData)
        println(actual)
        assertEquals(expected, actual.size)
    }

    @Test
    fun `invoke - value はデフォルト値, データ は1件`() {
        val expected = 1

        dbTestUtil.database.insertAndGenerateKeyProgram()
        val inputData = GetListProgramUseCase.InputData()
        val actual = useCase(inputData = inputData)
        println(actual)
        assertEquals(expected, actual.size)
    }

    @Test
    fun `invoke - value は'gr_', データ は1件`() {
        val expected = 1

        dbTestUtil.database.insertAndGenerateKeyProgram()
        val inputData = GetListProgramUseCase.InputData(
            value = "gr%"
        )
        val actual = useCase(inputData = inputData)
        println(actual)
        assertEquals(expected, actual.size)
    }

    @Test
    fun `invoke - limit は5, データ は10件`() {
        val expected = 5

        repeat(10) {
            dbTestUtil.database.insertAndGenerateKeyProgram(name = "greeting-$it")
        }
        val inputData = GetListProgramUseCase.InputData(
            limit = 5
        )
        val actual = useCase(inputData = inputData)
        println(actual)
        assertEquals(expected, actual.size)
    }

    @Test
    fun `invoke - OrderBy はASC, データ は10件`() {
        val expected = "greeting-0"

        repeat(10) {
            dbTestUtil.database.insertAndGenerateKeyProgram(name = "greeting-$it")
        }
        val inputData = GetListProgramUseCase.InputData(
            orderBy = OrderBy.ASC
        )
        val actual = useCase(inputData = inputData)
        println(actual)
        assertEquals(expected, actual.first().name)
    }

    @Test
    fun `invoke - OrderBy はDESC, データ は10件`() {
        val expected = "greeting-9"

        repeat(10) {
            dbTestUtil.database.insertAndGenerateKeyProgram(name = "greeting-$it")
        }
        val inputData = GetListProgramUseCase.InputData(
            orderBy = OrderBy.DESC
        )
        val actual = useCase(inputData = inputData)
        println(actual)
        assertEquals(expected, actual.first().name)
    }

    private val dbTestUtil = DBTestUtil()

    private lateinit var useCase: GetListProgramUseCase

    @Before
    fun beforeEach() {
        dbTestUtil.setUp()
        useCase = GetListProgramInteractor(
            provider = DBFindProgramDataProvider(database = dbTestUtil.database)
        )
    }

    @After
    fun afterEach() {
        dbTestUtil.tearDown()
    }

}
