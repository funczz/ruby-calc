package com.github.funczz.ruby_calc.core.interactor.element

import com.github.funczz.ruby_calc.core.usecase.element.SaveElementUseCase
import com.github.funczz.ruby_calc.data.db.entity.element
import com.github.funczz.ruby_calc.data.provider.element.DBRWElementDataProvider
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
import util.DBTestUtil.Companion.insertAndGenerateKeyProblem
import util.DBTestUtil.Companion.insertAndGenerateKeyProgram

@RunWith(RobolectricTestRunner::class)
@Config(
    manifest = "src/main/AndroidManifest.xml",
    packageName = "com.github.funczz.ruby_calc.android"
)
class SaveElementInteractorTest {

    @Test
    fun invoke() {
        val expected = 5
        val programId = dbTestUtil.database.insertAndGenerateKeyProgram()
        val problemId = dbTestUtil.database.insertAndGenerateKeyProblem(programId = programId)
        val values = mutableListOf<String>()
        repeat(expected) {
            values.add("$it, hello world.")
        }
        val inputData = SaveElementUseCase.InputData(
            problemId = problemId,
            values = values,
        )
        val actual = useCase(inputData = inputData)
        assertEquals(expected, actual)
        assertEquals(
            expected,
            dbTestUtil.database.element.filter { it.problemId eq problemId }.count()
        )
    }

    private val dbTestUtil = DBTestUtil()

    private lateinit var useCase: SaveElementUseCase

    @Before
    fun beforeEach() {
        dbTestUtil.setUp()
        useCase = SaveElementInteractor(
            provider = DBRWElementDataProvider(database = dbTestUtil.database)
        )
    }

    @After
    fun afterEach() {
        dbTestUtil.tearDown()
    }

}
