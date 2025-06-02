package com.github.funczz.ruby_calc.core.interactor.answer

import com.github.funczz.ruby_calc.core.usecase.answer.GetDetailsAnswerUseCase
import com.github.funczz.ruby_calc.data.provider.answer.DBCRUDAnswerDataProvider
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

@RunWith(RobolectricTestRunner::class)
@Config(
    manifest = "src/main/AndroidManifest.xml",
    packageName = "com.github.funczz.ruby_calc.android"
)
class GetDetailsAnswerInteractorTest {

    @Test
    fun invoke() {
        val programId = dbTestUtil.database.insertAndGenerateKeyProgram()
        val problemId = dbTestUtil.database.insertAndGenerateKeyProblem(programId = programId)
        val id = dbTestUtil.database.insertAndGenerateKeyAnswer(problemId = problemId)
        val inputData = GetDetailsAnswerUseCase.InputData(
            id = id
        )
        val actual = useCase(inputData = inputData)
        assertEquals(problemId, actual.get().problemId)
    }

    private val dbTestUtil = DBTestUtil()

    private lateinit var useCase: GetDetailsAnswerUseCase

    @Before
    fun beforeEach() {
        dbTestUtil.setUp()
        useCase = GetDetailsAnswerInteractor(
            provider = DBCRUDAnswerDataProvider(database = dbTestUtil.database)
        )
    }

    @After
    fun afterEach() {
        dbTestUtil.tearDown()
    }

}
