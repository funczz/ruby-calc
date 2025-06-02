package com.github.funczz.ruby_calc.core.interactor.setting

import com.github.funczz.ruby_calc.core.model.problem.ProblemIndex
import com.github.funczz.ruby_calc.core.model.setting.SettingItems
import com.github.funczz.ruby_calc.core.usecase.setting.ProblemIndexLoadSettingUseCase
import com.github.funczz.ruby_calc.core.vo.Column
import com.github.funczz.ruby_calc.core.vo.OrderBy
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
class ProblemIndexLoadSettingInteractorTest {

    @Test
    fun invoke() {
        val value = "hello world."
        val column = Column.CREATED_AT
        val orderBy = OrderBy.DESC
        val limit = 50
        dbTestUtil.database.insertSetting(
            name = SettingItems.PROBLEM_INDEX_VALUE.name,
            index = 0,
            value = value,
        )
        dbTestUtil.database.insertSetting(
            name = SettingItems.PROBLEM_INDEX_COLUMN.name,
            index = 0,
            value = column.name
        )
        dbTestUtil.database.insertSetting(
            name = SettingItems.PROBLEM_INDEX_ORDER_BY.name,
            index = 0,
            value = orderBy.name
        )
        dbTestUtil.database.insertSetting(
            name = SettingItems.PROBLEM_INDEX_LIMIT.name,
            index = 0,
            value = limit.toString(),
        )

        val inputData = ProblemIndexLoadSettingUseCase.InputData(
            problemIndex = ProblemIndex()
        )
        val actual = useCase(inputData = inputData)
        assertEquals(value, actual.value)
        assertEquals(column, actual.orderColumn)
        assertEquals(orderBy, actual.orderBy)
        assertEquals(limit, actual.limit)
    }

    private val dbTestUtil = DBTestUtil()

    private lateinit var useCase: ProblemIndexLoadSettingUseCase

    @Before
    fun beforeEach() {
        dbTestUtil.setUp()
        useCase = ProblemIndexLoadSettingInteractor(
            LoadSettingInteractor(
                provider = DBRWSettingDataProvider(database = dbTestUtil.database)
            )
        )
    }

    @After
    fun afterEach() {
        dbTestUtil.tearDown()
    }

}