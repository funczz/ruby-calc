package com.github.funczz.ruby_calc.data.provider.element

import com.github.funczz.ruby_calc.core.data.provider.element.RWElementDataProvider
import com.github.funczz.ruby_calc.data.db.entity.element
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
import util.DBTestUtil.Companion.insertAndGenerateKeyProblem
import util.DBTestUtil.Companion.insertAndGenerateKeyProgram
import util.DBTestUtil.Companion.insertElement

@Suppress("NonAsciiCharacters")
@RunWith(RobolectricTestRunner::class)
@Config(
    manifest = "src/main/AndroidManifest.xml",
    packageName = "com.github.funczz.ruby_calc.android"
)
class DBRWElementDataProviderTest {

    @Test
    fun read() {
        val expected = 5
        val programId = dbTestUtil.database.insertAndGenerateKeyProgram()
        val problemId = dbTestUtil.database.insertAndGenerateKeyProblem(programId = programId)
        repeat(expected) {
            dbTestUtil.database.insertElement(
                problemId = problemId,
                index = it,
                value = "$it, hello world."
            )
        }
        //read
        val actual = provider.read(problemId = problemId)
        assertEquals(expected, actual.size)
        for ((idx, v) in actual.withIndex()) {
            assertEquals(idx, v.elementIndex)
            assertEquals("$idx, hello world.", v.elementValue)
        }
    }

    @Test
    fun write() {
        val expected = 5
        val programId = dbTestUtil.database.insertAndGenerateKeyProgram()
        val problemId = dbTestUtil.database.insertAndGenerateKeyProblem(programId = programId)
        //write
        val values = mutableListOf<String>()
        repeat(expected) {
            values.add("$it, hello world.")
        }
        val actual = provider.write(problemId = problemId, values = values)
        assertEquals(expected, actual)
        assertEquals(
            expected,
            dbTestUtil.database.element.filter { it.problemId eq problemId }.count()
        )
    }

    @Test
    fun `write, read - 既存より多い要素を書き込む`() {
        val expected = 5
        val programId = dbTestUtil.database.insertAndGenerateKeyProgram()
        val problemId = dbTestUtil.database.insertAndGenerateKeyProblem(programId = programId)
        repeat(2) {
            dbTestUtil.database.insertElement(
                problemId = problemId,
                index = it,
                value = "$it, hello world."
            )
        }
        //write
        val values = mutableListOf<String>()
        repeat(expected) {
            values.add("$it, HELLO WORLD.")
        }
        provider.write(problemId = problemId, values = values)
        //read
        val actual = provider.read(problemId = problemId)
        assertEquals(expected, actual.size)
        for ((idx, v) in actual.withIndex()) {
            assertEquals(idx, v.elementIndex)
            assertEquals("$idx, HELLO WORLD.", v.elementValue)
        }
    }

    @Test
    fun `write, read - 既存より少ない要素を書き込む`() {
        val expected = 2
        val programId = dbTestUtil.database.insertAndGenerateKeyProgram()
        val problemId = dbTestUtil.database.insertAndGenerateKeyProblem(programId = programId)
        repeat(5) {
            dbTestUtil.database.insertElement(
                problemId = problemId,
                index = it,
                value = "$it, hello world."
            )
        }
        //write
        val values = mutableListOf<String>()
        repeat(expected) {
            values.add("$it, HELLO WORLD.")
        }
        provider.write(problemId = problemId, values = values)
        //read
        val actual = provider.read(problemId = problemId)
        assertEquals(expected, actual.size)
        for ((idx, v) in actual.withIndex()) {
            assertEquals(idx, v.elementIndex)
            assertEquals("$idx, HELLO WORLD.", v.elementValue)
        }
    }

    @Test
    fun `write, read - 要素を空にする`() {
        val expected = 0
        val programId = dbTestUtil.database.insertAndGenerateKeyProgram()
        val problemId = dbTestUtil.database.insertAndGenerateKeyProblem(programId = programId)
        repeat(5) {
            dbTestUtil.database.insertElement(
                problemId = problemId,
                index = it,
                value = "$it, hello world."
            )
        }
        //write
        val values = listOf<String>()
        provider.write(problemId = problemId, values = values)
        //read
        val actual = provider.read(problemId = problemId)
        assertEquals(expected, actual.size)
    }

    private val dbTestUtil = DBTestUtil()

    private lateinit var provider: RWElementDataProvider

    @Before
    fun beforeEach() {
        dbTestUtil.setUp()
        provider = DBRWElementDataProvider(database = dbTestUtil.database)
    }

    @After
    fun afterEach() {
        dbTestUtil.tearDown()
    }

}
