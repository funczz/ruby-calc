package com.github.funczz.ruby_calc.core.data.provider.setting

import com.github.funczz.ruby_calc.core.model.setting.SettingModel

interface RWSettingDataProvider {

    fun read(name: String): List<SettingModel>

    fun write(name: String, values: List<String>): Int

}