package com.github.funczz.ruby_calc.core.interactor.answer

import com.github.funczz.ruby_calc.core.usecase.answer.DeleteAnswerUseCase
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
import util.DBTestUtil.Companion.insertAndGenerateKeyAnswer
import util.DBTestUtil.Companion.insertAndGenerateKeyProblem
import util.DBTestUtil.Companion.insertAndGenerateKeyProgram

@RunWith(RobolectricTestRunner::class)
@Config(
    manifest = "src/main/AndroidManifest.xml",
    packageName = "com.github.funczz.ruby_calc.android"
)
class DeleteAnswerInteractorTest {

    @Test
    fun invoke() {
        val programId = dbTestUtil.database.insertAndGenerateKeyProgram()
        val problemId = dbTestUtil.database.insertAndGenerateKeyProblem(programId = programId)
        val id = dbTestUtil.database.insertAndGenerateKeyAnswer(problemId = problemId)
        assertEquals(1, dbTestUtil.database.answer.count())
        val inputData = DeleteAnswerUseCase.InputData(
            id = id
        )
        val actual = useCase(inputData = inputData)
        assertEquals(1, actual)
        assertEquals(0, dbTestUtil.database.answer.count())
    }

    private val dbTestUtil = DBTestUtil()

    private lateinit var useCase: DeleteAnswerUseCase

    @Before
    fun beforeEach() {
        dbTestUtil.setUp()
        useCase = DeleteAnswerInteractor(
            provider = DBCRUDAnswerDataProvider(database = dbTestUtil.database)
        )
    }

    @After
    fun afterEach() {
        dbTestUtil.tearDown()
    }

}
