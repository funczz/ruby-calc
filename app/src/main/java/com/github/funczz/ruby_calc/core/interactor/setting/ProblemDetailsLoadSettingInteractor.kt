package com.github.funczz.ruby_calc.core.interactor.setting

import com.github.funczz.ruby_calc.core.model.setting.SettingItems
import com.github.funczz.ruby_calc.core.usecase.setting.LoadSettingUseCase
import com.github.funczz.ruby_calc.core.usecase.setting.ProblemDetailsLoadSettingUseCase
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

class ProblemDetailsLoadSettingInteractor(

    private val useCase: LoadSettingUseCase

) : ProblemDetailsLoadSettingUseCase {

    override fun invoke(inputData: ProblemDetailsLoadSettingUseCase.InputData): Optional<Long> {
        //id
        val id = useCase(
            inputData = LoadSettingUseCase.InputData(settingName = SettingItems.PROBLEM_DETAILS_ID.name)
        ).let {
            try {
                it.firstOrNull()?.settingValue?.toLong() ?: inputData.defaultId.getOrNull()
            } catch (_: NumberFormatException) {
                inputData.defaultId.getOrNull()
            }
        }

        return Optional.ofNullable(id)
    }

}
