package com.github.funczz.ruby_calc.data.db.entity

import com.github.funczz.ruby_calc.core.model.setting.SettingModel
import com.github.funczz.ruby_calc.data.db.table.SettingTable
import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf

interface SettingEntity : Entity<SettingEntity> {
    companion object : Entity.Factory<SettingEntity>()

    var settingName: String

    var settingIndex: Int

    var settingValue: String

}

fun SettingEntity.getModel(): SettingModel {
    return SettingModel(
        settingName = this.settingName,
        settingIndex = this.settingIndex,
        settingValue = this.settingValue,
    )
}

fun SettingEntity.compareTo(
    settingName: String,
    settingIndex: Int,
    settingValue: String,
): Boolean {
    if (this.settingName != settingName) return false
    if (this.settingIndex != settingIndex) return false
    if (this.settingValue != settingValue) return false
    return true
}

val Database.setting get() = this.sequenceOf(SettingTable)