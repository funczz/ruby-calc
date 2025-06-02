package com.github.funczz.ruby_calc.core.interactor.setting

import com.github.funczz.ruby_calc.core.usecase.setting.LoadSettingUseCase
import com.github.funczz.ruby_calc.data.provider.setting.DBRWSettingDataProvider
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import util.DBTestUtil
import util.DBTestUtil.Companion.insertSetting

@RunWith(RobolectricTestRunner::class)
@Config(
    manifest = "src/main/AndroidManifest.xml",
    packageName = "com.github.funczz.ruby_calc.android"
)
class LoadSettingInteractorTest {

    @Test
    fun invoke() {
        val expected = 5
        val settingName = "setting"
        repeat(expected) {
            dbTestUtil.database.insertSetting(
                name = settingName,
                index = it,
                value = "$it, hello world."
            )
        }
        val inputData = LoadSettingUseCase.InputData(
            settingName = settingName
        )
        val actual = useCase(inputData = inputData)
        assertEquals(expected, actual.size)
    }

    private val dbTestUtil = DBTestUtil()

    private lateinit var useCase: LoadSettingUseCase

    @Before
    fun beforeEach() {
        dbTestUtil.setUp()
        useCase = LoadSettingInteractor(
            provider = DBRWSettingDataProvider(database = dbTestUtil.database)
        )
    }

    @After
    fun afterEach() {
        dbTestUtil.tearDown()
    }

}