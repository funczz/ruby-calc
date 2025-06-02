package com.github.funczz.ruby_calc.core.model.program

import com.github.funczz.ruby_calc.core.usecase.program.DeleteProgramUseCase
import com.github.funczz.ruby_calc.core.usecase.program.GetDetailsProgramUseCase
import com.github.funczz.ruby_calc.core.usecase.program.GetListProgramUseCase
import com.github.funczz.ruby_calc.core.usecase.program.SaveProgramUseCase
import com.github.funczz.ruby_calc.data.db.entity.program
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
import util.DBTestUtil.Companion.insertAndGenerateKeyProgram
import util.RubyCalcStateModelUtil

@RunWith(RobolectricTestRunner::class)
@Config(
    manifest = "src/main/AndroidManifest.xml",
    packageName = "com.github.funczz.ruby_calc.android"
)
class ProgramStateModelTest {

    @Test
    fun getDetails() {
        val id = dbTestUtil.database.insertAndGenerateKeyProgram(name = "greeting")
        assertEquals(1, dbTestUtil.database.program.count())
        val inputData =
            ProgramStateData.InputData(value = GetDetailsProgramUseCase.IdInputData(id = id))
        model.present(data = inputData)

        assertEquals(id, model.programDetails.programModel.get().id)
    }

    @Test
    fun getList() {
        dbTestUtil.database.insertAndGenerateKeyProgram(name = "greeting")
        assertEquals(1, dbTestUtil.database.program.count())
        val inputData = ProgramStateData.InputData(value = GetListProgramUseCase.InputData())
        model.present(data = inputData)

        assertEquals(1, model.programIndex.programList.size)
    }

    @Test
    fun `save - create`() {
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
        model.present(data = inputData)

        assertEquals(1, dbTestUtil.database.program.count())
        assertEquals(true, model.programSaveResult.id.isPresent)
    }

    @Test
    fun `save - name is already exists`() {
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
        model.present(data = inputData)
        model.present(data = inputData)

        assertEquals(1, dbTestUtil.database.program.count())
        assertEquals(false, model.programSaveResult.id.isPresent)
        assertEquals(true, model.programSaveResult.existsName)
    }

    @Test
    fun `save - update`() {
        val id = dbTestUtil.database.insertAndGenerateKeyProgram(name = "greeting")
        assertEquals(1, dbTestUtil.database.program.count())
        val inputData = ProgramStateData.InputData(
            value = SaveProgramUseCase.InputData(
                id = id,
                name = "GREETING",
                description = "greeting(EN)",
                hint = "-",
                code = "hello world.",
                createdAt = null,
                updatedAt = null,
            )
        )
        model.present(data = inputData)

        assertEquals(1, dbTestUtil.database.program.count())
        assertEquals(1, dbTestUtil.database.program.filter { it.name eq "GREETING" }.count())
        assertEquals(id, model.programSaveResult.id.get())
    }

    @Test
    fun delete() {
        val id = dbTestUtil.database.insertAndGenerateKeyProgram(name = "greeting")
        assertEquals(1, dbTestUtil.database.program.count())
        val inputData = ProgramStateData.InputData(value = DeleteProgramUseCase.InputData(id = id))
        model.present(data = inputData)

        assertEquals(0, dbTestUtil.database.program.count())
    }

    private val dbTestUtil = DBTestUtil()

    private lateinit var model: ProgramStateModel

    @Before
    fun beforeEach() {
        dbTestUtil.setUp()
        model = RubyCalcStateModelUtil.new(dbTestUtil = dbTestUtil).programStateModel
    }

    @After
    fun afterEach() {
        dbTestUtil.tearDown()
    }

}
