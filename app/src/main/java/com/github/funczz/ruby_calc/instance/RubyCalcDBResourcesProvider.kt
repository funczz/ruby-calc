package com.github.funczz.ruby_calc.instance

import android.util.Log
import com.github.funczz.ruby_calc.android.MainApplication
import com.github.funczz.ruby_calc.core.instance.InstanceHolder
import com.github.funczz.ruby_calc.core.instance.InstanceProvider
import com.github.funczz.ruby_calc.data.db.RubyCalcDBResources
import java.io.File
import java.util.concurrent.Executor

@Suppress("Unused", "MemberVisibilityCanBePrivate")
object RubyCalcDBResourcesProvider : InstanceProvider<RubyCalcDBResources> {

    private val TAG = RubyCalcDBResourcesProvider::class.simpleName

    override val name: String = this::class.qualifiedName!!

    override fun getInstanceHolder(): InstanceHolder {
        return MainApplication.getInstance()
    }

    fun new(filesDirectory: File, executor: Executor): RubyCalcDBResources {
        val dbResources = RubyCalcDBResources(filesDirectory = filesDirectory.canonicalFile)
        Log.d(TAG, "JDBC: Url=%s".format(dbResources.dataSource.connection.metaData.url))
        executor.run {
            dbResources.migration.initialize()
            Log.d(
                TAG,
                "SQLMigration: before migrate: currentVersionId=%s".format(
                    dbResources.migration.getCurrentVersionId()
                )
            )
            dbResources.migration.migrate()
            Log.d(
                TAG,
                "SQLMigration: after migrate: currentVersionId=%s".format(
                    dbResources.migration.getCurrentVersionId()
                )
            )

        }
        return dbResources
    }

    fun clear(filesDirectory: File) {
        RubyCalcDBResources.getDatabaseDirectoryFile(filesDirectory = filesDirectory.canonicalFile)
            .also {
                if (it.exists()) it.deleteRecursively()
            }
    }
}
