package com.github.funczz.ruby_calc.data.provider.setting

import com.github.funczz.kotlin.logging.LoggerFactory
import com.github.funczz.ruby_calc.core.data.provider.setting.RWSettingDataProvider
import com.github.funczz.ruby_calc.core.model.setting.SettingModel
import com.github.funczz.ruby_calc.data.db.entity.SettingEntity
import com.github.funczz.ruby_calc.data.db.entity.compareTo
import com.github.funczz.ruby_calc.data.db.entity.getModel
import com.github.funczz.ruby_calc.data.db.entity.setting
import com.github.funczz.ruby_calc.data.db.table.SettingTable
import com.github.funczz.ruby_calc.data.db.util.DatabaseExt
import org.ktorm.database.Database
import org.ktorm.dsl.and
import org.ktorm.dsl.asc
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.greaterEq
import org.ktorm.dsl.orderBy
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import org.ktorm.entity.add
import org.ktorm.entity.count
import org.ktorm.entity.filter
import org.ktorm.entity.find
import org.ktorm.entity.removeIf
import org.ktorm.entity.update

class DBRWSettingDataProvider(

    database: Database

) : RWSettingDataProvider, DatabaseExt {

    private val entitySequence = database.setting

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun read(name: String): List<SettingModel> {
        val resultSet = entitySequence.database.from(SettingTable)
            .select()
            .where { SettingTable.settingName eq name }
            .orderBy(SettingTable.settingIndex.asc())
        val settingList = mutableListOf<SettingModel>()
        for (row in resultSet) {
            settingList.add(SettingTable.createEntity(row).getModel())
        }
        logger.info { "%s=%s".format(name, settingList) }
        return settingList
    }

    override fun write(name: String, values: List<String>): Int {
        entitySequence.database.commit {
            entitySequence.removeIf { it.settingIndex greaterEq values.size }

            for ((index, value) in values.withIndex()) {
                val setting = entitySequence.find {
                    (it.settingName eq name) and (it.settingIndex eq index)
                }
                when (setting) {
                    is SettingEntity -> if (!setting.compareTo(
                            settingName = name,
                            settingIndex = index,
                            settingValue = value,
                        )
                    ) {
                        logger.info {
                            "Update: name=%s, index=%d, value=%s".format(
                                name,
                                index,
                                value
                            )
                        }
                        setting.settingValue = value
                        entitySequence.update(entity = setting)
                    } else {
                        logger.info {
                            "Same: name=%s, index=%d, value=%s".format(
                                name,
                                index,
                                value
                            )
                        }
                    }

                    else -> {
                        logger.info {
                            "Add: name=%s, index=%d, value=%s".format(
                                name,
                                index,
                                value
                            )
                        }
                        entitySequence.add(
                            entity = SettingEntity {
                                this.settingName = name
                                this.settingIndex = index
                                this.settingValue = value
                            }
                        )
                    }
                }
            }
        }
        val count = entitySequence.filter { it.settingName eq name }.count()
        logger.info {
            read(name = name)
            "row=%d".format(count)
        }
        return count
    }
}