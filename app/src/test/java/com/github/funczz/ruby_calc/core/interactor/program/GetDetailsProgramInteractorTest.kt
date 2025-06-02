package com.github.funczz.ruby_calc.core.interactor.program

import com.github.funczz.ruby_calc.core.usecase.program.GetDetailsProgramUseCase
import com.github.funczz.ruby_calc.data.provider.program.DBCRUDProgramDataProvider
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import util.DBTestUtil
import util.DBTestUtil.Companion.insertAndGenerateKeyProgram

@RunWith(RobolectricTestRunner::class)
@Config(
    manifest = "src/main/AndroidManifest.xml",
    packageName = "com.github.funczz.ruby_calc.android"
)
class GetDetailsProgramInteractorTest {

    @Test
    fun invoke() {
        val id = dbTestUtil.database.insertAndGenerateKeyProgram()
        val inputData = GetDetailsProgramUseCase.IdInputData(
            id = id
        )
        val actual = useCase(inputData = inputData)
        println(actual)
        assertEquals(id, actual.get().id)
    }

    private val dbTestUtil = DBTestUtil()

    private lateinit var useCase: GetDetailsProgramUseCase

    @Before
    fun beforeEach() {
        dbTestUtil.setUp()
        useCase = GetDetailsProgramInteractor(
            provider = DBCRUDProgramDataProvider(database = dbTestUtil.database)
        )
    }

    @After
    fun afterEach() {
        dbTestUtil.tearDown()
    }

}
