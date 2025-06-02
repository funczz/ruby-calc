package com.github.funczz.ruby_calc.data.db.table

import com.github.funczz.ruby_calc.data.db.RubyCalcDBResources
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.ktorm.database.Database
import org.ktorm.dsl.from
import org.ktorm.dsl.insert
import org.ktorm.dsl.select
import org.ktorm.entity.count
import org.ktorm.entity.sequenceOf
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.File
import java.util.concurrent.TimeUnit

@RunWith(RobolectricTestRunner::class)
@Config(
    manifest = "src/main/AndroidManifest.xml",
    packageName = "com.github.funczz.ruby_calc.android"
)
class SettingTableTest {

    @Test
    fun insert() {
        val settingName = "test"
        val settingIndex = 0
        val settingValue = "VALUE"
        //insert
        val result = database.useTransaction {
            database.insert(SettingTable) {
                set(it.settingName, settingName)
                set(it.settingIndex, settingIndex)
                set(it.settingValue, settingValue)
            }
        }
        assertEquals(1, result)
        assertEquals(1, database.sequenceOf(SettingTable).count())
        for (row in database.from(SettingTable).select()) {
            assertEquals(settingName, row[SettingTable.settingName])
            assertEquals(settingIndex, row[SettingTable.settingIndex])
            assertEquals(settingValue, row[SettingTable.settingValue])
        }
    }

    private val filesDirectory = File(".", "build")

    private val dbDir = File(filesDirectory, RubyCalcDBResources.DATABASE_DIRECTORY)

    private lateinit var dbResources: RubyCalcDBResources

    private lateinit var database: Database

    @Before
    fun beforeEach() {
        if (dbDir.exists()) dbDir.deleteRecursively()
        TimeUnit.MICROSECONDS.sleep(100L)
        assertTrue(!dbDir.exists())

        dbResources = RubyCalcDBResources(
            filesDirectory = filesDirectory,
        )
        dbResources.setupDB()
        database = dbResources.database
    }

    @After
    fun afterEach() {
        dbResources.shutdownDB()
        TimeUnit.MICROSECONDS.sleep(100L)
    }

}