package com.github.funczz.ruby_calc.data.provider

import com.github.funczz.ruby_calc.core.data.provider.BackupDataProvider
import com.github.funczz.ruby_calc.data.db.entity.answer
import com.github.funczz.ruby_calc.data.db.entity.element
import com.github.funczz.ruby_calc.data.db.entity.problem
import com.github.funczz.ruby_calc.data.db.entity.program
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
import util.DBTestUtil.Companion.insertAndGenerateKeyAnswer
import util.DBTestUtil.Companion.insertAndGenerateKeyProblem
import util.DBTestUtil.Companion.insertAndGenerateKeyProgram
import util.DBTestUtil.Companion.insertElement
import java.io.File

@RunWith(RobolectricTestRunner::class)
@Config(
    manifest = "src/main/AndroidManifest.xml",
    packageName = "com.github.funczz.ruby_calc.android"
)
class DBBackupDataProviderTest {

    @Test
    fun `backup & restore`() {
        //データを投入する
        val programIds = mutableListOf<Long>()
        repeat(10) {
            programIds.add(dbTestUtil.database.insertAndGenerateKeyProgram(name = "greeting-$it"))
        }
        val problemIds = mutableListOf<Long>()
        programIds.forEach {
            problemIds.add(
                dbTestUtil.database.insertAndGenerateKeyProblem(
                    programId = it,
                    name = "get greeting-$it"
                )
            )
        }
        problemIds.forEach { id ->
            repeat(5) { idx ->
                dbTestUtil.database.insertElement(
                    problemId = id,
                    index = idx,
                    value = "problemId=%d, index=%d".format(id, idx)
                )
            }
        }
        problemIds.forEach { id ->
            dbTestUtil.database.insertAndGenerateKeyAnswer(
                problemId = id,
                value = "problemId=%d".format(id)
            )
        }
        //バックアップする
        backupDataProvider.backup(
            filesDirectory = dbTestUtil.filesDirectory,
            backup = backupFile
        )
        assertEquals(true, backupFile.exists())
        //バックアップ後にデータを更新する
        dbTestUtil.database.useTransaction { t ->
            dbTestUtil.database.insertAndGenerateKeyProgram(name = "greeting-a").also { programId ->
                dbTestUtil.database.insertAndGenerateKeyProblem(
                    programId = programId,
                    name = "get greeting-a"
                ).also {
                    dbTestUtil.database.insertElement(problemId = it, index = 0, value = "a")
                    dbTestUtil.database.insertAndGenerateKeyAnswer(problemId = it, value = "A")
                }
            }
            t.commit()
        }
        assertEquals(11, dbTestUtil.database.program.count())
        assertEquals(1, dbTestUtil.database.program.filter { it.name eq "greeting-a" }.count())
        assertEquals(1, dbTestUtil.database.problem.filter { it.name eq "get greeting-a" }.count())
        assertEquals(11, dbTestUtil.database.element.filter { it.elementIndex eq 0 }.count())
        assertEquals(11, dbTestUtil.database.answer.count())
        //リストアする
        backupDataProvider.restore(
            filesDirectory = dbTestUtil.filesDirectory,
            backup = backupFile
        )
        //バックアップ後の更新は反映されない
        assertEquals(10, dbTestUtil.database.program.count())
        assertEquals(0, dbTestUtil.database.program.filter { it.name eq "greeting-a" }.count())
        assertEquals(0, dbTestUtil.database.problem.filter { it.name eq "get greeting-a" }.count())
        assertEquals(10, dbTestUtil.database.element.filter { it.elementIndex eq 0 }.count())
        assertEquals(10, dbTestUtil.database.answer.count())
    }

    private val dbTestUtil = DBTestUtil()

    private lateinit var backupDataProvider: BackupDataProvider

    private val backupFile = File(dbTestUtil.filesDirectory, "backup.zip")

    @Before
    fun beforeEach() {
        if (backupFile.exists()) backupFile.delete()
        dbTestUtil.setUp()
        backupDataProvider = DBBackupDataProvider(rubyCalcDBResources = dbTestUtil.dbResources)
    }

    @After
    fun afterEach() {
        dbTestUtil.tearDown()
    }

}
