package com.github.funczz.ruby_calc.core.interactor.setting

import com.github.funczz.ruby_calc.core.model.program.ProgramIndex
import com.github.funczz.ruby_calc.core.model.setting.SettingItems
import com.github.funczz.ruby_calc.core.usecase.setting.ProgramIndexSaveSettingUseCase
import com.github.funczz.ruby_calc.core.vo.Column
import com.github.funczz.ruby_calc.core.vo.OrderBy
import com.github.funczz.ruby_calc.data.db.entity.getModel
import com.github.funczz.ruby_calc.data.db.entity.setting
import com.github.funczz.ruby_calc.data.provider.setting.DBRWSettingDataProvider
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.ktorm.dsl.eq
import org.ktorm.entity.filter
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import util.DBTestUtil

@RunWith(RobolectricTestRunner::class)
@Config(
    manifest = "src/main/AndroidManifest.xml",
    packageName = "com.github.funczz.ruby_calc.android"
)
class ProgramIndexSaveSettingInteractorTest {

    @Test
    fun invoke() {
        val value = "hello world."
        val column = Column.CREATED_AT
        val orderBy = OrderBy.DESC
        val limit = 50
        val inputData = ProgramIndexSaveSettingUseCase.InputData(
            programIndex = ProgramIndex(
                value = value,
                orderColumn = column,
                orderBy = orderBy,
                limit = limit
            )
        )
        useCase(inputData = inputData)
        assertEquals(value, getList(SettingItems.PROGRAM_INDEX_VALUE).firstOrNull())
        assertEquals(column.name, getList(SettingItems.PROGRAM_INDEX_COLUMN).firstOrNull())
        assertEquals(orderBy.name, getList(SettingItems.PROGRAM_INDEX_ORDER_BY).firstOrNull())
        assertEquals(limit.toString(), getList(SettingItems.PROGRAM_INDEX_LIMIT).firstOrNull())
    }

    private fun getList(settingItem: SettingItems): List<String> {
        val result = mutableListOf<String>()
        for (row in dbTestUtil.database.setting.filter { it.settingName eq settingItem.name }) {
            result.add(row.getModel().settingValue)
        }
        return result
    }

    private val dbTestUtil = DBTestUtil()

    private lateinit var useCase: ProgramIndexSaveSettingUseCase

    @Before
    fun beforeEach() {
        dbTestUtil.setUp()
        useCase = ProgramIndexSaveSettingInteractor(
            SaveSettingInteractor(
                provider = DBRWSettingDataProvider(database = dbTestUtil.database)
            )
        )
    }

    @After
    fun afterEach() {
        dbTestUtil.tearDown()
    }

}