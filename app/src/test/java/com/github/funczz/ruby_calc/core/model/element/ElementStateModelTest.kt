package com.github.funczz.ruby_calc.core.model.element

import com.github.funczz.ruby_calc.core.usecase.element.GetDetailsElementUseCase
import com.github.funczz.ruby_calc.core.usecase.element.SaveElementUseCase
import com.github.funczz.ruby_calc.data.db.entity.element
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
import util.DBTestUtil.Companion.insertElement
import util.RubyCalcStateModelUtil

@RunWith(RobolectricTestRunner::class)
@Config(
    manifest = "src/main/AndroidManifest.xml",
    packageName = "com.github.funczz.ruby_calc.android"
)
class ElementStateModelTest {

    @Test
    fun getDetails() {
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
        val inputData = ElementStateData.InputData(
            value = GetDetailsElementUseCase.InputData(problemId = problemId)
        )
        model.present(data = inputData)

        assertEquals(10, model.elementDetails.elementList.size)
        assertEquals(problemId, model.elementDetails.elementList.first().problemId)
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
        model.present(data = inputData)

        assertEquals(2, dbTestUtil.database.element.count())
        assertEquals(0, model.elementDetails.elementList.size)
    }

    private val dbTestUtil = DBTestUtil()

    private lateinit var model: ElementStateModel

    @Before
    fun beforeEach() {
        dbTestUtil.setUp()
        model = RubyCalcStateModelUtil.new(dbTestUtil = dbTestUtil).elementStateModel
    }

    @After
    fun afterEach() {
        dbTestUtil.tearDown()
    }

}
