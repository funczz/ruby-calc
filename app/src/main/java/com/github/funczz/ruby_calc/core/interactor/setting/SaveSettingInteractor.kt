package com.github.funczz.ruby_calc.core.interactor.setting

import com.github.funczz.ruby_calc.core.data.provider.setting.RWSettingDataProvider
import com.github.funczz.ruby_calc.core.usecase.setting.SaveSettingUseCase

class SaveSettingInteractor(

    private val provider: RWSettingDataProvider

) : SaveSettingUseCase {

    override fun invoke(inputData: SaveSettingUseCase.InputData): Int {
        return provider.write(inputData.settingName, inputData.values)
    }

}