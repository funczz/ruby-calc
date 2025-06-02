package com.github.funczz.ruby_calc.core.interactor.problem

import com.github.funczz.ruby_calc.core.usecase.problem.SaveProblemUseCase
import com.github.funczz.ruby_calc.data.db.entity.problem
import com.github.funczz.ruby_calc.data.provider.problem.DBCRUDProblemDataProvider
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.ktorm.entity.count
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import util.DBTestUtil
import util.DBTestUtil.Companion.insertAndGenerateKeyProgram

@RunWith(RobolectricTestRunner::class)
@Config(
    manifest = "src/main/AndroidManifest.xml",
    packageName = "com.github.funczz.ruby_calc.android"
)
class SaveProgramInteractorTest {

    @Test
    fun invoke() {
        val programId = dbTestUtil.database.insertAndGenerateKeyProgram()
        val inputData = SaveProblemUseCase.InputData(
            id = null,
            name = "greeting",
            programId = programId,
            comment = "-",
            createdAt = null,
            updatedAt = null,
        )
        val actual = useCase(inputData = inputData)
        assertEquals(1, actual)
        assertEquals(1, dbTestUtil.database.problem.count())
    }

    private val dbTestUtil = DBTestUtil()

    private lateinit var useCase: SaveProblemUseCase

    @Before
    fun beforeEach() {
        dbTestUtil.setUp()
        useCase = SaveProblemInteractor(
            provider = DBCRUDProblemDataProvider(database = dbTestUtil.database)
        )
    }

    @After
    fun afterEach() {
        dbTestUtil.tearDown()
    }

}
