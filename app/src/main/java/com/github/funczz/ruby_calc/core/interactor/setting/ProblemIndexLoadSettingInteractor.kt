package com.github.funczz.ruby_calc.core.interactor.setting

import com.github.funczz.ruby_calc.core.model.problem.ProblemIndex
import com.github.funczz.ruby_calc.core.model.setting.SettingItems
import com.github.funczz.ruby_calc.core.model.setting.SettingModel
import com.github.funczz.ruby_calc.core.usecase.setting.LoadSettingUseCase
import com.github.funczz.ruby_calc.core.usecase.setting.ProblemIndexLoadSettingUseCase
import com.github.funczz.ruby_calc.core.vo.Column
import com.github.funczz.ruby_calc.core.vo.OrderBy

class ProblemIndexLoadSettingInteractor(

    private val useCase: LoadSettingUseCase

) : ProblemIndexLoadSettingUseCase {

    override fun invoke(inputData: ProblemIndexLoadSettingUseCase.InputData): ProblemIndex {
        //value
        val value = useCase(
            inputData = LoadSettingUseCase.InputData(settingName = SettingItems.PROBLEM_INDEX_VALUE.name)
        ).let {
            it.firstOrNull()?.settingValue ?: inputData.problemIndex.value
        }
        //column
        val column = useCase(
            inputData = LoadSettingUseCase.InputData(settingName = SettingItems.PROBLEM_INDEX_COLUMN.name)
        ).let {
            when (val v = it.firstOrNull()) {
                is SettingModel -> when (v.settingValue) {
                    Column.NAME.name -> Column.NAME
                    Column.CREATED_AT.name -> Column.CREATED_AT
                    Column.UPDATED_AT.name -> Column.UPDATED_AT
                    else -> inputData.problemIndex.orderColumn
                }

                else -> inputData.problemIndex.orderColumn
            }
        }
        //orderBy
        val orderBy = useCase(
            inputData = LoadSettingUseCase.InputData(settingName = SettingItems.PROBLEM_INDEX_ORDER_BY.name)
        ).let {
            when (val v = it.firstOrNull()) {
                is SettingModel -> when (v.settingValue) {
                    OrderBy.ASC.name -> OrderBy.ASC
                    OrderBy.DESC.name -> OrderBy.DESC
                    else -> inputData.problemIndex.orderBy
                }

                else -> inputData.problemIndex.orderBy
            }
        }
        //limit
        val limit = useCase(
            inputData = LoadSettingUseCase.InputData(settingName = SettingItems.PROBLEM_INDEX_LIMIT.name)
        ).let {
            try {
                it.firstOrNull()?.settingValue?.toInt() ?: inputData.problemIndex.limit
            } catch (_: NumberFormatException) {
                inputData.problemIndex.limit
            }
        }

        return inputData.problemIndex.copy(
            problemList = listOf(),
            value = value,
            orderColumn = column,
            orderBy = orderBy,
            limit = limit,
        )
    }

}