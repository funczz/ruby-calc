package com.github.funczz.ruby_calc.core.interactor.answer

import com.github.funczz.ruby_calc.core.usecase.answer.SaveAnswerUseCase
import com.github.funczz.ruby_calc.data.db.entity.answer
import com.github.funczz.ruby_calc.data.provider.answer.DBCRUDAnswerDataProvider
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
class SaveAnswerInteractorTest {

    @Test
    fun invoke() {
        val programId = dbTestUtil.database.insertAndGenerateKeyProgram()
        val problemId = dbTestUtil.database.insertAndGenerateKeyProblem(programId = programId)
        val inputData = SaveAnswerUseCase.InputData(
            problemId = problemId,
            value = "hello world."
        )
        val actual = useCase(inputData = inputData)
        assertEquals(1, actual)
        assertEquals(1, dbTestUtil.database.answer.count())
    }

    private val dbTestUtil = DBTestUtil()

    private lateinit var useCase: SaveAnswerUseCase

    @Before
    fun beforeEach() {
        dbTestUtil.setUp()
        useCase = SaveAnswerInteractor(
            provider = DBCRUDAnswerDataProvider(database = dbTestUtil.database)
        )
    }

    @After
    fun afterEach() {
        dbTestUtil.tearDown()
    }

}
