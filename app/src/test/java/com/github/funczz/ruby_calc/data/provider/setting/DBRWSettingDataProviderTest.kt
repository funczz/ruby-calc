package com.github.funczz.ruby_calc.data.provider.setting

import com.github.funczz.ruby_calc.core.data.provider.setting.RWSettingDataProvider
import com.github.funczz.ruby_calc.data.db.entity.setting
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
import util.DBTestUtil.Companion.insertSetting

@Suppress("NonAsciiCharacters")
@RunWith(RobolectricTestRunner::class)
@Config(
    manifest = "src/main/AndroidManifest.xml",
    packageName = "com.github.funczz.ruby_calc.android"
)
class DBRWSettingDataProviderTest {

    @Test
    fun read() {
        val expected = 5
        val settingName = "setting"
        repeat(expected) {
            dbTestUtil.database.insertSetting(
                name = settingName,
                index = it,
                value = "$it, hello world."
            )
        }
        //read
        val actual = provider.read(name = settingName)
        assertEquals(expected, actual.size)
        for ((idx, v) in actual.withIndex()) {
            assertEquals(idx, v.settingIndex)
            assertEquals("$idx, hello world.", v.settingValue)
        }
    }

    @Test
    fun write() {
        val expected = 5
        val settingName = "setting"
        //write
        val values = mutableListOf<String>()
        repeat(expected) {
            values.add("$it, hello world.")
        }
        val actual = provider.write(name = settingName, values = values)
        assertEquals(expected, actual)
        assertEquals(
            expected,
            dbTestUtil.database.setting.filter { it.settingName eq settingName }.count()
        )
    }

    @Test
    fun `write, read - 既存より多い要素を書き込む`() {
        val expected = 5
        val settingName = "setting"
        repeat(2) {
            dbTestUtil.database.insertSetting(
                name = settingName,
                index = it,
                value = "$it, hello world."
            )
        }
        //write
        val values = mutableListOf<String>()
        repeat(expected) {
            values.add("$it, HELLO WORLD.")
        }
        provider.write(name = settingName, values = values)
        //read
        val actual = provider.read(name = settingName)
        assertEquals(expected, actual.size)
        for ((idx, v) in actual.withIndex()) {
            assertEquals(idx, v.settingIndex)
            assertEquals("$idx, HELLO WORLD.", v.settingValue)
        }
    }

    @Test
    fun `write, read - 既存より少ない要素を書き込む`() {
        val expected = 2
        val settingName = "setting"
        repeat(5) {
            dbTestUtil.database.insertSetting(
                name = settingName,
                index = it,
                value = "$it, hello world."
            )
        }
        //write
        val values = mutableListOf<String>()
        repeat(expected) {
            values.add("$it, HELLO WORLD.")
        }
        provider.write(name = settingName, values = values)
        //read
        val actual = provider.read(name = settingName)
        assertEquals(expected, actual.size)
        for ((idx, v) in actual.withIndex()) {
            assertEquals(idx, v.settingIndex)
            assertEquals("$idx, HELLO WORLD.", v.settingValue)
        }
    }

    @Test
    fun `write, read - 要素を空にする`() {
        val expected = 0
        val settingName = "setting"
        repeat(5) {
            dbTestUtil.database.insertSetting(
                name = settingName,
                index = it,
                value = "$it, hello world."
            )
        }
        //write
        val values = listOf<String>()
        provider.write(name = settingName, values = values)
        //read
        val actual = provider.read(name = settingName)
        assertEquals(expected, actual.size)
    }

    private val dbTestUtil = DBTestUtil()

    private lateinit var provider: RWSettingDataProvider

    @Before
    fun beforeEach() {
        dbTestUtil.setUp()
        provider = DBRWSettingDataProvider(database = dbTestUtil.database)
    }

    @After
    fun afterEach() {
        dbTestUtil.tearDown()
    }

}