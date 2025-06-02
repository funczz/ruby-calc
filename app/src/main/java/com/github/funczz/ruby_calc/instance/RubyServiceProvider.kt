package com.github.funczz.ruby_calc.instance

import com.github.funczz.ruby_calc.android.MainApplication
import com.github.funczz.ruby_calc.core.instance.InstanceHolder
import com.github.funczz.ruby_calc.core.instance.InstanceProvider
import com.github.funczz.ruby_calc.core.service.RubyService

object RubyServiceProvider : InstanceProvider<RubyService> {

    override val name: String = this::class.qualifiedName!!

    override fun getInstanceHolder(): InstanceHolder {
        return MainApplication.getInstance()
    }

    fun new(): RubyService = RubyService()

}
