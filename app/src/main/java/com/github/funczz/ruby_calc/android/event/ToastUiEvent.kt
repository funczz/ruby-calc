package com.github.funczz.ruby_calc.android.event

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

@Suppress("Unused")
data class ToastUiEvent(
    val id: Int? = null,
    val message: String = "",
    val duration: Int = Toast.LENGTH_SHORT,
) : UiEvent {

    override var isDone: Boolean = false

    @Composable
    fun Show(context: Context) {
        isDone = true
        val msg = when (id) {
            is Int -> stringResource(id = id)
            else -> message
        }
        Toast.makeText(context, msg, duration).show()
    }

}
