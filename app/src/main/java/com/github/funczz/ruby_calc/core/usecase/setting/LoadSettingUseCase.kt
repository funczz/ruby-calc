package com.github.funczz.ruby_calc.core.usecase.setting

import com.github.funczz.ruby_calc.core.model.setting.SettingModel

interface LoadSettingUseCase :
    SettingUseCase<LoadSettingUseCase.InputData, List<SettingModel>> {

    data class InputData(
        val settingName: String
    ) : SettingUseCase.InputData

}