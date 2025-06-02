package com.github.funczz.ruby_calc.core.interactor.setting

import com.github.funczz.ruby_calc.core.model.program.ProgramIndex
import com.github.funczz.ruby_calc.core.model.setting.SettingItems
import com.github.funczz.ruby_calc.core.model.setting.SettingModel
import com.github.funczz.ruby_calc.core.usecase.setting.LoadSettingUseCase
import com.github.funczz.ruby_calc.core.usecase.setting.ProgramIndexLoadSettingUseCase
import com.github.funczz.ruby_calc.core.vo.Column
import com.github.funczz.ruby_calc.core.vo.OrderBy

class ProgramIndexLoadSettingInteractor(

    private val useCase: LoadSettingUseCase

) : ProgramIndexLoadSettingUseCase {

    override fun invoke(inputData: ProgramIndexLoadSettingUseCase.InputData): ProgramIndex {
        //value
        val value = useCase(
            inputData = LoadSettingUseCase.InputData(settingName = SettingItems.PROGRAM_INDEX_VALUE.name)
        ).let {
            it.firstOrNull()?.settingValue ?: inputData.programIndex.value
        }
        //column
        val column = useCase(
            inputData = LoadSettingUseCase.InputData(settingName = SettingItems.PROGRAM_INDEX_COLUMN.name)
        ).let {
            when (val v = it.firstOrNull()) {
                is SettingModel -> when (v.settingValue) {
                    Column.NAME.name -> Column.NAME
                    Column.CREATED_AT.name -> Column.CREATED_AT
                    Column.UPDATED_AT.name -> Column.UPDATED_AT
                    else -> inputData.programIndex.orderColumn
                }

                else -> inputData.programIndex.orderColumn
            }
        }
        //orderBy
        val orderBy = useCase(
            inputData = LoadSettingUseCase.InputData(settingName = SettingItems.PROGRAM_INDEX_ORDER_BY.name)
        ).let {
            when (val v = it.firstOrNull()) {
                is SettingModel -> when (v.settingValue) {
                    OrderBy.ASC.name -> OrderBy.ASC
                    OrderBy.DESC.name -> OrderBy.DESC
                    else -> inputData.programIndex.orderBy
                }

                else -> inputData.programIndex.orderBy
            }
        }
        //limit
        val limit = useCase(
            inputData = LoadSettingUseCase.InputData(settingName = SettingItems.PROGRAM_INDEX_LIMIT.name)
        ).let {
            try {
                it.firstOrNull()?.settingValue?.toInt() ?: inputData.programIndex.limit
            } catch (_: NumberFormatException) {
                inputData.programIndex.limit
            }
        }

        return inputData.programIndex.copy(
            programList = listOf(),
            value = value,
            orderColumn = column,
            orderBy = orderBy,
            limit = limit,
        )
    }

}