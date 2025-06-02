package com.github.funczz.ruby_calc.instance

import com.github.funczz.ruby_calc.android.MainApplication
import com.github.funczz.ruby_calc.core.instance.InstanceHolder
import com.github.funczz.ruby_calc.core.instance.InstanceProvider
import com.github.funczz.ruby_calc.core.service.BackupService
import com.github.funczz.ruby_calc.data.db.RubyCalcDBResources
import com.github.funczz.ruby_calc.data.provider.DBBackupDataProvider
import java.io.File

object BackupServiceProvider : InstanceProvider<BackupService> {

    override val name: String = this::class.qualifiedName!!

    override fun getInstanceHolder(): InstanceHolder {
        return MainApplication.getInstance()
    }

    fun new(dbResources: RubyCalcDBResources, filesDirectory: File): BackupService {
        return BackupService(
            dbBackupDataProvider = DBBackupDataProvider(rubyCalcDBResources = dbResources),
            filesDirectory = filesDirectory,
        )
    }
}
