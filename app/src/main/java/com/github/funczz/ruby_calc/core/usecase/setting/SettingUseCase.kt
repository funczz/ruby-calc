package com.github.funczz.ruby_calc.core.usecase.setting

import com.github.funczz.ruby_calc.core.usecase.UseCase

interface SettingUseCase<I : SettingUseCase.InputData, O : Any> : UseCase<I, O> {

    interface InputData : UseCase.InputData
}