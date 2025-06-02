package com.github.funczz.ruby_calc.core.interactor.program

import com.github.funczz.ruby_calc.core.usecase.program.DeleteProgramUseCase
import com.github.funczz.ruby_calc.data.db.entity.program
import com.github.funczz.ruby_calc.data.provider.program.DBCRUDProgramDataProvider
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
class DeleteProgramInteractorTest {

    @Test
    fun invoke() {
        val id = dbTestUtil.database.insertAndGenerateKeyProgram()
        assertEquals(1, dbTestUtil.database.program.count())
        val inputData = DeleteProgramUseCase.InputData(
            id = id
        )
        val actual = useCase(inputData = inputData)
        assertEquals(1, actual)
        assertEquals(0, dbTestUtil.database.program.count())
    }

    private val dbTestUtil = DBTestUtil()

    private lateinit var useCase: DeleteProgramUseCase

    @Before
    fun beforeEach() {
        dbTestUtil.setUp()
        useCase = DeleteProgramInteractor(
            provider = DBCRUDProgramDataProvider(database = dbTestUtil.database)
        )
    }

    @After
    fun afterEach() {
        dbTestUtil.tearDown()
    }

}
