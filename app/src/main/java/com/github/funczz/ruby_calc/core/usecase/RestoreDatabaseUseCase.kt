package com.github.funczz.ruby_calc.core.usecase

import java.io.File

interface RestoreDatabaseUseCase : DatabaseUseCase<RestoreDatabaseUseCase.InputData> {

    data class InputData(
        val filesDirectory: File,
        val backupFile: File,
    ) : DatabaseUseCase.InputData

}
