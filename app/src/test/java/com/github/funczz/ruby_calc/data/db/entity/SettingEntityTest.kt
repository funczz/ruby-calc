package com.github.funczz.ruby_calc.data.db.entity

import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.ktorm.database.Database
import org.ktorm.entity.add
import org.ktorm.entity.count
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import util.DBTestUtil

@RunWith(RobolectricTestRunner::class)
@Config(
    manifest = "src/main/AndroidManifest.xml",
    packageName = "com.github.funczz.ruby_calc.android"
)
class SettingEntityTest {

    @Test
    fun add() {
        val settingName = "test"
        val settingIndex = 0
        val settingValue = "VALUE"
        database.useTransaction {
            try {
                //add
                database.setting.add(
                    entity = SettingEntity {
                        this.settingName = settingName
                        this.settingIndex = settingIndex
                        this.settingValue = settingValue
                    }
                )
                it.commit()
            } catch (th: Throwable) {
                it.rollback()
            }
        }

        assertEquals(1, database.setting.count())
    }

    private val dbTestUtil = DBTestUtil()

    private lateinit var database: Database

    @Before
    fun beforeEach() {
        dbTestUtil.setUp()
        database = dbTestUtil.database
    }

    @After
    fun afterEach() {
        dbTestUtil.tearDown()
    }

}