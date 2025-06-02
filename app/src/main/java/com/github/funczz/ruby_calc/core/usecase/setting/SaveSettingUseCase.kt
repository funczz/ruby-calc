package com.github.funczz.ruby_calc.core.usecase.setting

interface SaveSettingUseCase : SettingUseCase<SaveSettingUseCase.InputData, Int> {

    data class InputData(
        val settingName: String,
        val values: List<String>,
    ) : SettingUseCase.InputData

}