package com.github.funczz.ruby_calc.core.interactor.problem

import com.github.funczz.ruby_calc.core.usecase.problem.GetDetailsProblemUseCase
import com.github.funczz.ruby_calc.data.provider.problem.DBCRUDProblemDataProvider
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
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
class GetDetailsProblemInteractorTest {

    @Test
    fun invoke() {
        val programId = dbTestUtil.database.insertAndGenerateKeyProgram()
        val id = dbTestUtil.database.insertAndGenerateKeyProblem(programId = programId)
        val inputData = GetDetailsProblemUseCase.IdInputData(
            id = id
        )
        val actual = useCase(inputData = inputData)
        assertEquals(programId, actual.get().programId)
    }

    private val dbTestUtil = DBTestUtil()

    private lateinit var useCase: GetDetailsProblemUseCase

    @Before
    fun beforeEach() {
        dbTestUtil.setUp()
        useCase = GetDetailsProblemInteractor(
            provider = DBCRUDProblemDataProvider(database = dbTestUtil.database)
        )
    }

    @After
    fun afterEach() {
        dbTestUtil.tearDown()
    }

}
