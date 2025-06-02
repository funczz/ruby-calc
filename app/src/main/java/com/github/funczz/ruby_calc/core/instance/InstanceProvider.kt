package com.github.funczz.ruby_calc.core.instance

interface InstanceProvider<T : Any> {

    val name: String

    fun getInstanceHolder(): InstanceHolder

    @Suppress("UNCHECKED_CAST")
    fun getInstance(): T {
        return getInstanceHolder().instances[name] as T
    }

    fun onInit(function: () -> T) {
        getInstanceHolder().instances[name] = function()
    }
}
