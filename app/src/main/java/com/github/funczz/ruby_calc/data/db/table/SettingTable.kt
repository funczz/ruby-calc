package com.github.funczz.ruby_calc.data.db.table

import com.github.funczz.ruby_calc.data.db.entity.SettingEntity
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object SettingTable : Table<SettingEntity>("setting") {

    val settingName = varchar("setting_name").primaryKey().bindTo { it.settingName }

    val settingIndex = int("setting_index").primaryKey().bindTo { it.settingIndex }

    val settingValue = varchar("setting_value").bindTo { it.settingValue }

}