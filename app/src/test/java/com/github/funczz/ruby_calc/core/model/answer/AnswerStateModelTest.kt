package com.github.funczz.ruby_calc.core.model.answer

import com.github.funczz.ruby_calc.core.usecase.answer.DeleteAnswerUseCase
import com.github.funczz.ruby_calc.core.usecase.answer.GetDetailsAnswerUseCase
import com.github.funczz.ruby_calc.core.usecase.answer.GetListAnswerUseCase
import com.github.funczz.ruby_calc.core.usecase.answer.SaveAnswerUseCase
import com.github.funczz.ruby_calc.data.db.entity.answer
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
import util.DBTestUtil.Companion.insertAndGenerateKeyAnswer
import util.DBTestUtil.Companion.insertAndGenerateKeyProblem
import util.DBTestUtil.Companion.insertAndGenerateKeyProgram
import util.RubyCalcStateModelUtil

@RunWith(RobolectricTestRunner::class)
@Config(
    manifest = "src/main/AndroidManifest.xml",
    packageName = "com.github.funczz.ruby_calc.android"
)
class ProblemStateModelTest {

    @Test
    fun getDetails() {
        val programId = dbTestUtil.database.insertAndGenerateKeyProgram()
        val problemId = dbTestUtil.database.insertAndGenerateKeyProblem(programId = programId)
        val id = dbTestUtil.database.insertAndGenerateKeyAnswer(problemId = problemId)
        assertEquals(1, dbTestUtil.database.answer.count())
        val inputData =
            AnswerStateData.InputData(value = GetDetailsAnswerUseCase.InputData(id = problemId))
        model.present(data = inputData)

        assertEquals(id, model.answerDetails.answerModel.get().id)
    }

    @Test
    fun getList() {
        val programId = dbTestUtil.database.insertAndGenerateKeyProgram()
        val problemId = dbTestUtil.database.insertAndGenerateKeyProblem(programId = programId)
        dbTestUtil.database.insertAndGenerateKeyAnswer(problemId = problemId)
        assertEquals(1, dbTestUtil.database.problem.count())
        val inputData =
            AnswerStateData.InputData(value = GetListAnswerUseCase.InputData(problemId = problemId))
        model.present(data = inputData)

        assertEquals(1, model.answerIndex.answerList.size)
    }

    @Test
    fun `save - create`() {
        val programId = dbTestUtil.database.insertAndGenerateKeyProgram()
        val problemId = dbTestUtil.database.insertAndGenerateKeyProblem(programId = programId)
        assertEquals(0, dbTestUtil.database.answer.count())
        val inputData = AnswerStateData.InputData(
            value = SaveAnswerUseCase.InputData(
                problemId = problemId,
                value = "hello world."
            )
        )
        model.present(data = inputData)

        assertEquals(1, dbTestUtil.database.answer.count())
    }

    @Test
    fun delete() {
        val programId = dbTestUtil.database.insertAndGenerateKeyProgram()
        val problemId = dbTestUtil.database.insertAndGenerateKeyProblem(programId = programId)
        val id = dbTestUtil.database.insertAndGenerateKeyAnswer(problemId = problemId)
        assertEquals(1, dbTestUtil.database.answer.count())
        val inputData = AnswerStateData.InputData(value = DeleteAnswerUseCase.InputData(id = id))
        model.present(data = inputData)

        assertEquals(0, dbTestUtil.database.answer.count())
    }

    private val dbTestUtil = DBTestUtil()

    private lateinit var model: AnswerStateModel

    @Before
    fun beforeEach() {
        dbTestUtil.setUp()
        model = RubyCalcStateModelUtil.new(dbTestUtil = dbTestUtil).answerStateModel
    }

    @After
    fun afterEach() {
        dbTestUtil.tearDown()
    }

}
