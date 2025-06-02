package com.github.funczz.ruby_calc.core.model

import com.github.funczz.ruby_calc.core.model.answer.AnswerStateData
import com.github.funczz.ruby_calc.core.model.element.ElementStateData
import com.github.funczz.ruby_calc.core.model.error.ErrorStateData
import com.github.funczz.ruby_calc.core.model.problem.ProblemStateData
import com.github.funczz.ruby_calc.core.model.program.ProgramStateData
import com.github.funczz.ruby_calc.core.model.ruby.RubyStateData
import com.github.funczz.ruby_calc.core.service.RubyService
import com.github.funczz.ruby_calc.core.usecase.answer.DeleteAnswerUseCase
import com.github.funczz.ruby_calc.core.usecase.answer.GetDetailsAnswerUseCase
import com.github.funczz.ruby_calc.core.usecase.answer.GetListAnswerUseCase
import com.github.funczz.ruby_calc.core.usecase.answer.SaveAnswerUseCase
import com.github.funczz.ruby_calc.core.usecase.element.GetDetailsElementUseCase
import com.github.funczz.ruby_calc.core.usecase.element.SaveElementUseCase
import com.github.funczz.ruby_calc.core.usecase.problem.DeleteProblemUseCase
import com.github.funczz.ruby_calc.core.usecase.problem.GetDetailsProblemUseCase
import com.github.funczz.ruby_calc.core.usecase.problem.GetListProblemUseCase
import com.github.funczz.ruby_calc.core.usecase.problem.SaveProblemUseCase
import com.github.funczz.ruby_calc.core.usecase.program.DeleteProgramUseCase
import com.github.funczz.ruby_calc.core.usecase.program.GetDetailsProgramUseCase
import com.github.funczz.ruby_calc.core.usecase.program.GetListProgramUseCase
import com.github.funczz.ruby_calc.core.usecase.program.SaveProgramUseCase
import com.github.funczz.ruby_calc.core.usecase.ruby.ExecuteRubyUseCase
import com.github.funczz.ruby_calc.data.db.entity.answer
import com.github.funczz.ruby_calc.data.db.entity.element
import com.github.funczz.ruby_calc.data.db.entity.problem
import com.github.funczz.ruby_calc.data.db.entity.program
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.ktorm.entity.count
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import util.DBTestUtil
import util.DBTestUtil.Companion.insertAndGenerateKeyAnswer
import util.DBTestUtil.Companion.insertAndGenerateKeyProblem
import util.DBTestUtil.Companion.insertAndGenerateKeyProgram
import util.DBTestUtil.Companion.insertElement
import util.RubyCalcStateModelUtil
import java.util.concurrent.TimeUnit

@RunWith(RobolectricTestRunner::class)
@Config(
    manifest = "src/main/AndroidManifest.xml",
    packageName = "com.github.funczz.ruby_calc.android"
)
class RubyCalcStateModelTest {

    /**
     * ProgramStateModel
     */

    @Test
    fun `getDetails program`() {
        val id = dbTestUtil.database.insertAndGenerateKeyProgram(name = "greeting")
        assertEquals(1, dbTestUtil.database.program.count())
        val inputData =
            ProgramStateData.InputData(value = GetDetailsProgramUseCase.IdInputData(id = id))
        stateModel.present(data = inputData)

        assertEquals(id, stateModel.programStateModel.programDetails.programModel.get().id)
        assertEquals(0, stateModel.programStateModel.programIndex.programList.size)
    }

    @Test
    fun `getList program`() {
        dbTestUtil.database.insertAndGenerateKeyProgram(name = "greeting")
        assertEquals(1, dbTestUtil.database.program.count())
        val inputData = ProgramStateData.InputData(value = GetListProgramUseCase.InputData())
        stateModel.present(data = inputData)

        assertEquals(1, stateModel.programStateModel.programIndex.programList.size)
        assertEquals(false, stateModel.programStateModel.programDetails.programModel.isPresent)
    }

    @Test
    fun `save program`() {
        assertEquals(0, dbTestUtil.database.program.count())
        val inputData = ProgramStateData.InputData(
            value = SaveProgramUseCase.InputData(
                id = null,
                name = "greeting",
                description = "greeting(EN)",
                hint = "-",
                code = "hello world.",
                createdAt = null,
                updatedAt = null,
            )
        )
        stateModel.present(data = inputData)

        assertEquals(1, dbTestUtil.database.program.count())
        assertEquals(1, stateModel.programStateModel.programIndex.programList.size)
    }

    @Test
    fun `delete program`() {
        val id = dbTestUtil.database.insertAndGenerateKeyProgram(name = "greeting")
        repeat(10) {
            dbTestUtil.database.insertAndGenerateKeyProgram(name = "greeting-$it")
        }
        assertEquals(11, dbTestUtil.database.program.count())
        val inputData = ProgramStateData.InputData(value = DeleteProgramUseCase.InputData(id = id))
        stateModel.present(data = inputData)

        assertEquals(10, dbTestUtil.database.program.count())
        assertEquals(10, stateModel.programStateModel.programIndex.programList.size)
    }

    /**
     * ProblemStateModel
     */

    @Test
    fun `getDetails problem`() {
        val programId = dbTestUtil.database.insertAndGenerateKeyProgram()
        val problemId = dbTestUtil.database.insertAndGenerateKeyProblem(programId = programId)
        assertEquals(1, dbTestUtil.database.problem.count())
        val inputData =
            ProblemStateData.InputData(value = GetDetailsProblemUseCase.IdInputData(id = problemId))
        stateModel.present(data = inputData)

        assertEquals(problemId, stateModel.problemStateModel.problemDetails.problemModel.get().id)
        assertEquals(0, stateModel.problemStateModel.problemIndex.problemList.size)
    }

    @Test
    fun `getList problem`() {
        val programId = dbTestUtil.database.insertAndGenerateKeyProgram()
        dbTestUtil.database.insertAndGenerateKeyProblem(programId = programId)
        assertEquals(1, dbTestUtil.database.problem.count())
        val inputData = ProblemStateData.InputData(value = GetListProblemUseCase.InputData())
        stateModel.present(data = inputData)

        assertEquals(1, stateModel.problemStateModel.problemIndex.problemList.size)
        assertEquals(false, stateModel.problemStateModel.problemDetails.problemModel.isPresent)
    }

    @Test
    fun `save problem`() {
        val programId = dbTestUtil.database.insertAndGenerateKeyProgram()
        assertEquals(0, dbTestUtil.database.problem.count())
        val inputData = ProblemStateData.InputData(
            value = SaveProblemUseCase.InputData(
                id = null,
                name = "get greeting",
                programId = programId,
                comment = "-",
                createdAt = null,
                updatedAt = null,
            )
        )
        stateModel.present(data = inputData)

        assertEquals(1, dbTestUtil.database.problem.count())
        assertEquals(1, stateModel.problemStateModel.problemIndex.problemList.size)
    }

    @Test
    fun `delete problem`() {
        val programId = dbTestUtil.database.insertAndGenerateKeyProgram()
        val problemId = dbTestUtil.database.insertAndGenerateKeyProblem(programId = programId)
        repeat(10) {
            dbTestUtil.database.insertAndGenerateKeyProblem(
                programId = programId,
                name = "get greeting-$it"
            )
        }
        assertEquals(11, dbTestUtil.database.problem.count())
        val inputData =
            ProblemStateData.InputData(value = DeleteProblemUseCase.InputData(id = problemId))
        stateModel.present(data = inputData)

        assertEquals(10, dbTestUtil.database.problem.count())
        assertEquals(10, stateModel.problemStateModel.problemIndex.problemList.size)
    }

    /**
     * ElementStateModel
     */

    @Test
    fun `getDetails element`() {
        val programId = dbTestUtil.database.insertAndGenerateKeyProgram()
        val problemId = dbTestUtil.database.insertAndGenerateKeyProblem(programId = programId)
        repeat(10) {
            dbTestUtil.database.insertElement(
                problemId = problemId,
                index = it,
                value = "value-$it"
            )
        }
        assertEquals(10, dbTestUtil.database.element.count())
        val inputData =
            ElementStateData.InputData(value = GetDetailsElementUseCase.InputData(problemId = problemId))
        stateModel.present(data = inputData)

        assertEquals(10, stateModel.elementStateModel.elementDetails.elementList.size)
        assertEquals(
            problemId,
            stateModel.elementStateModel.elementDetails.elementList.first().problemId
        )
    }

    @Test
    fun `save element`() {
        val programId = dbTestUtil.database.insertAndGenerateKeyProgram()
        val problemId = dbTestUtil.database.insertAndGenerateKeyProblem(programId = programId)
        assertEquals(0, dbTestUtil.database.element.count())
        val inputData = ElementStateData.InputData(
            value = SaveElementUseCase.InputData(
                problemId = problemId,
                values = listOf("0", "1")
            )
        )
        stateModel.present(data = inputData)

        assertEquals(2, dbTestUtil.database.element.count())
        assertEquals(0, stateModel.elementStateModel.elementDetails.elementList.size)
    }

    /**
     * AnswerStateModel
     */

    @Test
    fun `getDetails answer`() {
        val programId = dbTestUtil.database.insertAndGenerateKeyProgram()
        val problemId = dbTestUtil.database.insertAndGenerateKeyProblem(programId = programId)
        val answerId = dbTestUtil.database.insertAndGenerateKeyAnswer(problemId = problemId)
        assertEquals(1, dbTestUtil.database.answer.count())
        val inputData =
            AnswerStateData.InputData(value = GetDetailsAnswerUseCase.InputData(id = problemId))
        stateModel.present(data = inputData)

        assertEquals(answerId, stateModel.answerStateModel.answerDetails.answerModel.get().id)
        assertEquals(0, stateModel.answerStateModel.answerIndex.answerList.size)
    }

    @Test
    fun `getList answer`() {
        val programId = dbTestUtil.database.insertAndGenerateKeyProgram()
        val problemId = dbTestUtil.database.insertAndGenerateKeyProblem(programId = programId)
        dbTestUtil.database.insertAndGenerateKeyAnswer(problemId = problemId)
        assertEquals(1, dbTestUtil.database.answer.count())
        val inputData =
            AnswerStateData.InputData(value = GetListAnswerUseCase.InputData(problemId = problemId))
        stateModel.present(data = inputData)

        assertEquals(1, stateModel.answerStateModel.answerIndex.answerList.size)
        assertEquals(false, stateModel.answerStateModel.answerDetails.answerModel.isPresent)
    }

    @Test
    fun `save answer`() {
        val programId = dbTestUtil.database.insertAndGenerateKeyProgram()
        val problemId = dbTestUtil.database.insertAndGenerateKeyProblem(programId = programId)
        assertEquals(0, dbTestUtil.database.answer.count())
        val inputData = AnswerStateData.InputData(
            value = SaveAnswerUseCase.InputData(
                problemId = problemId,
                value = "hello world."
            )
        )
        stateModel.present(data = inputData)

        assertEquals(1, dbTestUtil.database.answer.count())
        assertEquals(0, stateModel.answerStateModel.answerIndex.answerList.size)
    }

    @Test
    fun `delete answer`() {
        val programId = dbTestUtil.database.insertAndGenerateKeyProgram()
        val problemId = dbTestUtil.database.insertAndGenerateKeyProblem(programId = programId)
        val answerId = dbTestUtil.database.insertAndGenerateKeyAnswer(problemId = problemId)
        repeat(10) {
            dbTestUtil.database.insertAndGenerateKeyAnswer(
                problemId = problemId,
                value = "$it, hello world."
            )
        }
        assertEquals(11, dbTestUtil.database.answer.count())
        val inputData = AnswerStateData.InputData(
            value = DeleteAnswerUseCase.InputData(id = answerId)
        )
        stateModel.present(data = inputData)

        assertEquals(10, dbTestUtil.database.answer.count())
        assertEquals(0, stateModel.answerStateModel.answerIndex.answerList.size)
    }

    /**
     * RubyStateModel
     */

    @Test
    fun executeRuby() {
        val expected = "hello world."
        val inputData = RubyStateData.InputData(
            value = ExecuteRubyUseCase.InputData(
                code = "'$expected'",
                argv = listOf(),
            )
        )
        stateModel.present(data = inputData)

        assertEquals(false, stateModel.rubyStateModel.rubyResult.failure.isPresent)
        assertEquals(expected, stateModel.rubyStateModel.rubyResult.success)
    }

    @Test
    fun throwEvalExceptionExecuteRuby() {
        val inputData = RubyStateData.InputData(
            value = ExecuteRubyUseCase.InputData(
                code = "",
                argv = listOf()
            )
        )
        stateModel.present(data = inputData)

        assertEquals(0, stateModel.errorStateModel.throwableList.size)
        assertEquals(
            RubyService.EvalException::class,
            stateModel.rubyStateModel.rubyResult.failure.get()::class
        )
    }

    /**
     * ErrorStateModel
     */

    @Test
    fun `present ErrorStateData_FunctionData`() {
        val expected = "ERROR!"
        stateModel.present(
            data = ErrorStateData.FunctionData { throw Exception(expected) }
        )
        assertEquals(expected, stateModel.errorStateModel.throwableList.firstOrNull()?.message)
    }

    @Test
    fun `present ErrorStateData_ThrowableData`() {
        val expected = "ERROR!"
        stateModel.present(
            data = ErrorStateData.ThrowableData(throwable = Exception(expected))
        )
        assertEquals(expected, stateModel.errorStateModel.throwableList.firstOrNull()?.message)
    }

    @Test
    fun `present ErrorStateData_InitializeData`() {
        val expected = "ERROR!"
        stateModel.present(
            data = ErrorStateData.InitializeData(throwableList = listOf(Exception(expected)))
        )
        assertEquals(expected, stateModel.errorStateModel.throwableList.firstOrNull()?.message)
    }


    private val dbTestUtil = DBTestUtil()

    private lateinit var stateModel: RubyCalcStateModel

    @Before
    fun beforeEach() {
        dbTestUtil.setUp()
        stateModel = RubyCalcStateModelUtil.new(dbTestUtil = dbTestUtil)
    }

    @After
    fun afterEach() {
        dbTestUtil.tearDown()
        TimeUnit.MICROSECONDS.sleep(200L)
    }

    companion object {

        private lateinit var rubyService: RubyService

        @BeforeClass
        @JvmStatic
        fun beforeAll() {
            rubyService = RubyService()
            TimeUnit.SECONDS.sleep(5L)
        }

    }
}
