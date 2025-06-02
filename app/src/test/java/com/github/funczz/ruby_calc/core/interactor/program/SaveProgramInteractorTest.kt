package com.github.funczz.ruby_calc.core.interactor.program

import com.github.funczz.ruby_calc.core.usecase.program.SaveProgramUseCase
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

@RunWith(RobolectricTestRunner::class)
@Config(
    manifest = "src/main/AndroidManifest.xml",
    packageName = "com.github.funczz.ruby_calc.android"
)
class SaveProgramInteractorTest {

    @Test
    fun invoke() {
        val inputData = SaveProgramUseCase.InputData(
            id = null,
            name = "greeting",
            description = "greeting(EN)",
            hint = "-",
            code = "hello world.",
            createdAt = null,
            updatedAt = null,
        )
        val actual = useCase(inputData = inputData)
        assertEquals(1, actual)
        assertEquals(1, dbTestUtil.database.program.count())
    }

    private val dbTestUtil = DBTestUtil()

    private lateinit var useCase: SaveProgramUseCase

    @Before
    fun beforeEach() {
        dbTestUtil.setUp()
        useCase = SaveProgramInteractor(
            provider = DBCRUDProgramDataProvider(database = dbTestUtil.database)
        )
    }

    @After
    fun afterEach() {
        dbTestUtil.tearDown()
    }

}
