package util

import com.github.funczz.ruby_calc.data.db.RubyCalcDBResources
import com.github.funczz.ruby_calc.data.db.entity.ElementEntity
import com.github.funczz.ruby_calc.data.db.entity.element
import com.github.funczz.ruby_calc.data.db.entity.problem
import com.github.funczz.ruby_calc.data.db.table.AnswerTable
import com.github.funczz.ruby_calc.data.db.table.ProblemTable
import com.github.funczz.ruby_calc.data.db.table.ProgramTable
import org.junit.Assert.assertTrue
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.insert
import org.ktorm.dsl.insertAndGenerateKey
import org.ktorm.entity.add
import org.ktorm.entity.find
import java.io.File
import java.util.concurrent.TimeUnit

@Suppress("Unused", "MemberVisibilityCanBePrivate")
open class DBTestUtil {

    val filesDirectory: File = Companion.filesDirectory

    val dbDir = File(filesDirectory, RubyCalcDBResources.DATABASE_DIRECTORY)

    lateinit var dbResources: RubyCalcDBResources

    lateinit var database: Database

    fun setUp() {
        if (dbDir.exists()) dbDir.deleteRecursively()
        TimeUnit.MICROSECONDS.sleep(100L)
        assertTrue(!dbDir.exists())

        dbResources = RubyCalcDBResources(
            filesDirectory = filesDirectory,
        )
        dbResources.setupDB()
        database = Database.connect(dbResources.dataSource)
    }

    fun tearDown() {
        dbResources.shutdownDB()
        TimeUnit.MICROSECONDS.sleep(100L)
    }

    companion object {

        val filesDirectory: File = File(".", "build").canonicalFile

        @JvmStatic
        fun assertWait(
            matches: () -> Boolean,
            assert: () -> Unit,
        ) {
            val timeout = 100L
            var condition = false
            for (i in 1..timeout) {
                TimeUnit.MILLISECONDS.sleep(i)
                condition = matches()
                if (condition) {
                    assert()
                    break
                }
            }
            assertTrue("テストを実施する条件が満たされている", condition)
        }

        fun Database.insertAndGenerateKeyProgram(name: String = "greeting"): Long {
            return this.useTransaction {
                this.insertAndGenerateKey(ProgramTable) {
                    set(it.name, name)
                    set(it.description, "greeting(EN)")
                    set(it.hint, "-")
                    set(it.code, "'hello world.'")
                } as Long
            }
        }

        fun Database.insertAndGenerateKeyProblem(
            programId: Long,
            name: String = "get greeting"
        ): Long {
            this.useTransaction {
                this.insert(ProblemTable) {
                    set(it.name, name)
                    set(it.programId, programId)
                    set(it.comment, "-")
                }
            }
            return this.problem.find { it.name eq name }!!.id
        }

        fun Database.insertElement(problemId: Long, index: Int, value: String) {
            this.useTransaction {
                this.element.add(
                    entity = ElementEntity {
                        this.problemId = problemId
                        this.elementIndex = index
                        this.elementValue = value
                    }
                )
            }
        }

        fun Database.insertAndGenerateKeyAnswer(
            problemId: Long,
            value: String = "hello world."
        ): Long {
            return this.useTransaction {
                this.insertAndGenerateKey(AnswerTable) {
                    set(it.problemId, problemId)
                    set(it.value, value)
                } as Long
            }
        }
    }
}
