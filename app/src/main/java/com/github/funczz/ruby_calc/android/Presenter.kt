package com.github.funczz.ruby_calc.android

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import kotlinx.coroutines.flow.StateFlow

interface Presenter<T : Any> {

    fun getStateFlow(): StateFlow<T>

    @Composable
    fun getState(): State<T>

    fun render(output: T)

}
