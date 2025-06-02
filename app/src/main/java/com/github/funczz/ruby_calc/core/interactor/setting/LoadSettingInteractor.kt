package com.github.funczz.ruby_calc.core.interactor.setting

import com.github.funczz.ruby_calc.core.data.provider.setting.RWSettingDataProvider
import com.github.funczz.ruby_calc.core.model.setting.SettingModel
import com.github.funczz.ruby_calc.core.usecase.setting.LoadSettingUseCase

class LoadSettingInteractor(

    private val provider: RWSettingDataProvider

) : LoadSettingUseCase {

    override fun invoke(inputData: LoadSettingUseCase.InputData): List<SettingModel> {
        return provider.read(inputData.settingName)
    }

}