package com.github.funczz.ruby_calc.core.interactor.setting

import com.github.funczz.ruby_calc.core.usecase.setting.SaveSettingUseCase
import com.github.funczz.ruby_calc.data.db.entity.setting
import com.github.funczz.ruby_calc.data.provider.setting.DBRWSettingDataProvider
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

@RunWith(RobolectricTestRunner::class)
@Config(
    manifest = "src/main/AndroidManifest.xml",
    packageName = "com.github.funczz.ruby_calc.android"
)
class SaveSettingInteractorTest {

    @Test
    fun invoke() {
        val expected = 5
        val settingName = "setting"
        val values = mutableListOf<String>()
        repeat(expected) {
            values.add("$it, hello world.")
        }
        val inputData = SaveSettingUseCase.InputData(
            settingName = settingName,
            values = values,
        )
        val actual = useCase(inputData = inputData)
        assertEquals(expected, actual)
        assertEquals(
            expected,
            dbTestUtil.database.setting.filter { it.settingName eq settingName }.count()
        )
    }

    private val dbTestUtil = DBTestUtil()

    private lateinit var useCase: SaveSettingUseCase

    @Before
    fun beforeEach() {
        dbTestUtil.setUp()
        useCase = SaveSettingInteractor(
            provider = DBRWSettingDataProvider(database = dbTestUtil.database)
        )
    }

    @After
    fun afterEach() {
        dbTestUtil.tearDown()
    }

}