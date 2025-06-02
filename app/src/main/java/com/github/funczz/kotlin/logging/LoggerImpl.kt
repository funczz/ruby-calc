package com.github.funczz.kotlin.logging

import java.util.logging.Level

@Suppress("Unused")
class LoggerImpl(private val logger: java.util.logging.Logger) : Logger {

    companion object {

        fun new(type: Class<*>): Logger {
            return LoggerImpl(java.util.logging.Logger.getLogger(type.name)).also {
                it.setReleaseLevel()
            }
        }
    }

    override fun setDebugLevel() {
        logger.level = Level.ALL
    }

    override fun setReleaseLevel() {
        logger.level = Level.SEVERE
    }

    //最も詳細
    override fun finest(function: () -> String) {
        logger.finest(function)
    }

    //詳細
    override fun finer(function: () -> String) {
        logger.finer(function)
    }

    //普通
    override fun fine(function: () -> String) {
        logger.fine(function)
    }

    //構成
    override fun config(function: () -> String) {
        logger.config(function)
    }

    //情報
    override fun info(function: () -> String) {
        logger.info(function)
    }

    //警告
    override fun warning(function: () -> String) {
        logger.warning(function)
    }

    //重大
    override fun severe(function: () -> String) {
        logger.severe(function)
    }

}
