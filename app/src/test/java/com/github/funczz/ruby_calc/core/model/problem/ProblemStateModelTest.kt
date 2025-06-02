package com.github.funczz.ruby_calc.core.model.problem

import com.github.funczz.ruby_calc.core.usecase.problem.DeleteProblemUseCase
import com.github.funczz.ruby_calc.core.usecase.problem.GetDetailsProblemUseCase
import com.github.funczz.ruby_calc.core.usecase.problem.GetListProblemUseCase
import com.github.funczz.ruby_calc.core.usecase.problem.SaveProblemUseCase
import com.github.funczz.ruby_calc.data.db.entity.problem
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.ktorm.dsl.eq
import org.ktorm.entity.count
import org.ktorm.entity.filter
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import util.DBTestUtil
import util.DBTestUtil.Companion.insertAndGenerateKeyAnswer
import util.DBTestUtil.Companion.insertAndGenerateKeyProblem
import util.DBTestUtil.Companion.insertAndGenerateKeyProgram
import util.DBTestUtil.Companion.insertElement
import util.RubyCalcStateModelUtil

@Suppress("NonAsciiCharacters")
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
        assertEquals(1, dbTestUtil.database.problem.count())
        val inputData = ProblemStateData.InputData(
            value = GetDetailsProblemUseCase.IdInputData(id = problemId)
        )
        model.present(data = inputData)

        assertEquals(problemId, model.problemDetails.problemModel.get().id)
        assertEquals(programId, model.problemDetails.programModel.get().id)
        assertEquals(true, model.problemDetails.canBeDeleted)
    }

    @Test
    fun `getDetails - 外部キーを持つElementModelがあるのでcanBeDeletedはfalseである`() {
        val programId = dbTestUtil.database.insertAndGenerateKeyProgram()
        val problemId = dbTestUtil.database.insertAndGenerateKeyProblem(programId = programId)
        dbTestUtil.database.insertElement(problemId = problemId, index = 0, value = "hello world.")
        assertEquals(1, dbTestUtil.database.problem.count())
        val inputData = ProblemStateData.InputData(
            value = GetDetailsProblemUseCase.IdInputData(id = problemId)
        )
        model.present(data = inputData)

        assertEquals(problemId, model.problemDetails.problemModel.get().id)
        assertEquals(programId, model.problemDetails.programModel.get().id)
        assertEquals(false, model.problemDetails.canBeDeleted)
    }

    @Test
    fun `getDetails - 外部キーを持つAnswerModelがあるのでcanBeDeletedはfalseである`() {
        val programId = dbTestUtil.database.insertAndGenerateKeyProgram()
        val problemId = dbTestUtil.database.insertAndGenerateKeyProblem(programId = programId)
        dbTestUtil.database.insertAndGenerateKeyAnswer(
            problemId = problemId,
            value = "hello world."
        )
        assertEquals(1, dbTestUtil.database.problem.count())
        val inputData = ProblemStateData.InputData(
            value = GetDetailsProblemUseCase.IdInputData(id = problemId)
        )
        model.present(data = inputData)

        assertEquals(problemId, model.problemDetails.problemModel.get().id)
        assertEquals(programId, model.problemDetails.programModel.get().id)
        assertEquals(false, model.problemDetails.canBeDeleted)
    }

    @Test
    fun getList() {
        val programId = dbTestUtil.database.insertAndGenerateKeyProgram()
        dbTestUtil.database.insertAndGenerateKeyProblem(programId = programId)
        assertEquals(1, dbTestUtil.database.problem.count())
        val inputData = ProblemStateData.InputData(
            value = GetListProblemUseCase.InputData()
        )
        model.present(data = inputData)

        assertEquals(1, model.problemIndex.problemList.size)
    }

    @Test
    fun `save - create`() {
        val programId = dbTestUtil.database.insertAndGenerateKeyProgram()
        assertEquals(0, dbTestUtil.database.problem.count())
        val inputData = ProblemStateData.InputData(
            value = SaveProblemUseCase.InputData(
                id = null,
                name = "greeting",
                programId = programId,
                comment = "-",
                createdAt = null,
                updatedAt = null,
            )
        )
        model.present(data = inputData)

        assertEquals(1, dbTestUtil.database.problem.count())
        assertEquals(true, model.problemSaveResult.id.isPresent)
    }

    @Test
    fun `save - name is already exists`() {
        val programId = dbTestUtil.database.insertAndGenerateKeyProgram()
        assertEquals(0, dbTestUtil.database.problem.count())
        val inputData = ProblemStateData.InputData(
            value = SaveProblemUseCase.InputData(
                id = null,
                name = "greeting",
                programId = programId,
                comment = "-",
                createdAt = null,
                updatedAt = null,
            )
        )
        model.present(data = inputData)
        model.present(data = inputData)

        assertEquals(1, dbTestUtil.database.problem.count())
        assertEquals(false, model.problemSaveResult.id.isPresent)
        assertEquals(true, model.problemSaveResult.existsName)
    }

    @Test
    fun `save - update`() {
        val programId = dbTestUtil.database.insertAndGenerateKeyProgram()
        val problemId = dbTestUtil.database.insertAndGenerateKeyProblem(programId = programId)
        assertEquals(1, dbTestUtil.database.problem.count())
        val inputData = ProblemStateData.InputData(
            value = SaveProblemUseCase.InputData(
                id = problemId,
                name = "GREETING",
                programId = programId,
                comment = "-",
                createdAt = null,
                updatedAt = null,
            )
        )
        model.present(data = inputData)

        assertEquals(1, dbTestUtil.database.problem.count())
        assertEquals(1, dbTestUtil.database.problem.filter { it.name eq "GREETING" }.count())
        assertEquals(problemId, model.problemSaveResult.id.get())
    }

    @Test
    fun delete() {
        val programId = dbTestUtil.database.insertAndGenerateKeyProgram()
        val problemId = dbTestUtil.database.insertAndGenerateKeyProblem(programId = programId)
        assertEquals(1, dbTestUtil.database.problem.count())
        val inputData = ProblemStateData.InputData(
            value = DeleteProblemUseCase.InputData(id = problemId)
        )
        model.present(data = inputData)

        assertEquals(0, dbTestUtil.database.problem.count())
    }

    private val dbTestUtil = DBTestUtil()

    private lateinit var model: ProblemStateModel

    @Before
    fun beforeEach() {
        dbTestUtil.setUp()
        model = RubyCalcStateModelUtil.new(dbTestUtil = dbTestUtil).problemStateModel
    }

    @After
    fun afterEach() {
        dbTestUtil.tearDown()
    }

}
