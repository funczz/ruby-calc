package com.github.funczz.kotlin.logging

import com.github.funczz.ruby_calc.android.BuildConfig

object LoggerFactory {

    fun getLogger(type: Class<*>): Logger {
        return LoggerImpl.new(type).also {
            when (BuildConfig.DEBUG) {
                true -> it.setDebugLevel()
                else -> it.setReleaseLevel()
            }
        }
    }

}
