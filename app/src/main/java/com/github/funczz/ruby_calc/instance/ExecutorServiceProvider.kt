package com.github.funczz.ruby_calc.instance

import com.github.funczz.ruby_calc.android.MainApplication
import com.github.funczz.ruby_calc.core.instance.InstanceHolder
import com.github.funczz.ruby_calc.core.instance.InstanceProvider
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

object ExecutorServiceProvider : InstanceProvider<ExecutorService> {

    override val name: String = this::class.qualifiedName!!

    override fun getInstanceHolder(): InstanceHolder {
        return MainApplication.getInstance()
    }

    fun new(): ExecutorService = Executors.newCachedThreadPool()
}
