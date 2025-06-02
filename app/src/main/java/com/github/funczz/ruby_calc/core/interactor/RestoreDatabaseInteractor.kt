package com.github.funczz.ruby_calc.core.interactor

import com.github.funczz.ruby_calc.core.data.provider.BackupDataProvider
import com.github.funczz.ruby_calc.core.usecase.RestoreDatabaseUseCase

class RestoreDatabaseInteractor(

    private val backupDataProvider: BackupDataProvider

) : RestoreDatabaseUseCase {

    override fun invoke(inputData: RestoreDatabaseUseCase.InputData) {
        backupDataProvider.restore(
            filesDirectory = inputData.filesDirectory,
            backup = inputData.backupFile,
        )
    }

}
