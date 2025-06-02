package com.github.funczz.ruby_calc.data.provider.problem

import com.github.funczz.ruby_calc.core.data.provider.program.FindProgramDataProvider
import com.github.funczz.ruby_calc.data.db.entity.program
import com.github.funczz.ruby_calc.data.provider.program.DBFindProgramDataProvider
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
class DBFindProgramDataProviderTest {

    @Test
    fun find() {
        repeat(5) {
            dbTestUtil.database.insertAndGenerateKeyProgram(name = "greeting $it")
        }
        repeat(5) {
            dbTestUtil.database.insertAndGenerateKeyProgram(name = "GREETING $it")
        }
        assertEquals(10, dbTestUtil.database.program.count())

        val actual = provider.find("GREETING %")
        assertEquals(5, actual.size)
    }

    private val dbTestUtil = DBTestUtil()

    private lateinit var provider: FindProgramDataProvider

    @Before
    fun beforeEach() {
        dbTestUtil.setUp()
        provider = DBFindProgramDataProvider(database = dbTestUtil.database)
    }

    @After
    fun afterEach() {
        dbTestUtil.tearDown()
    }

}
