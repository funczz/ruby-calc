package com.github.funczz.kotlin.logging

interface Logger {

    fun setDebugLevel()

    fun setReleaseLevel()

    //最も詳細
    fun finest(function: () -> String)

    //詳細
    fun finer(function: () -> String)

    //普通
    fun fine(function: () -> String)

    //構成
    fun config(function: () -> String)

    //情報
    fun info(function: () -> String)

    //警告
    fun warning(function: () -> String)

    //重大
    fun severe(function: () -> String)

}
