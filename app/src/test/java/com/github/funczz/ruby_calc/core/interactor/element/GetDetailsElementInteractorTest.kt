package com.github.funczz.ruby_calc.core.interactor.element

import com.github.funczz.ruby_calc.core.usecase.element.GetDetailsElementUseCase
import com.github.funczz.ruby_calc.data.provider.element.DBRWElementDataProvider
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
import util.DBTestUtil.Companion.insertElement

@RunWith(RobolectricTestRunner::class)
@Config(
    manifest = "src/main/AndroidManifest.xml",
    packageName = "com.github.funczz.ruby_calc.android"
)
class GetDetailsElementInteractorTest {

    @Test
    fun invoke() {
        val expected = 5
        val programId = dbTestUtil.database.insertAndGenerateKeyProgram()
        val problemId = dbTestUtil.database.insertAndGenerateKeyProblem(programId = programId)
        repeat(expected) {
            dbTestUtil.database.insertElement(
                problemId = problemId,
                index = it,
                value = "$it, hello world."
            )
        }
        val inputData = GetDetailsElementUseCase.InputData(
            problemId = problemId
        )
        val actual = useCase(inputData = inputData)
        assertEquals(expected, actual.size)
    }

    private val dbTestUtil = DBTestUtil()

    private lateinit var useCase: GetDetailsElementUseCase

    @Before
    fun beforeEach() {
        dbTestUtil.setUp()
        useCase = GetDetailsElementInteractor(
            provider = DBRWElementDataProvider(database = dbTestUtil.database)
        )
    }

    @After
    fun afterEach() {
        dbTestUtil.tearDown()
    }

}
