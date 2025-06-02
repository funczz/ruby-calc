package com.github.funczz.ruby_calc.core.usecase

import java.io.File

interface BackupDatabaseUseCase : DatabaseUseCase<BackupDatabaseUseCase.InputData> {

    data class InputData(
        val filesDirectory: File,
        val backupFile: File,
    ) : DatabaseUseCase.InputData

}
