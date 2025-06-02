package com.github.funczz.ruby_calc.core.interactor

import com.github.funczz.ruby_calc.core.data.provider.BackupDataProvider
import com.github.funczz.ruby_calc.core.usecase.BackupDatabaseUseCase

class BackupDatabaseInteractor(

    private val backupDataProvider: BackupDataProvider

) : BackupDatabaseUseCase {

    override fun invoke(inputData: BackupDatabaseUseCase.InputData) {
        backupDataProvider.backup(
            filesDirectory = inputData.filesDirectory,
            backup = inputData.backupFile,
        )
    }

}
