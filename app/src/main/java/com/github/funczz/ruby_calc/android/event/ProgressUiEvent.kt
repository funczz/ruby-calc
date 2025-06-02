package com.github.funczz.ruby_calc.android.event

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.github.funczz.ruby_calc.android.ui.theme.RubyCalcTheme
import java.util.concurrent.Executor

@Suppress("Unused")
data class ProgressUiEvent(
    val executor: Executor,
    val backgroundAction: () -> Unit,
    val throwAction: (Throwable) -> Unit,
    val finallyAction: () -> Unit,
) : UiEvent {

    override var isDone: Boolean = false

    private var isStarted = false

    @Composable
    fun Show() {
        if (isDone) return
        if (!isStarted) startAction()
        ProgressDialog()
    }

    private fun startAction() {
        isStarted = true
        executor.execute {
            try {
                backgroundAction()
            } catch (th: Throwable) {
                throwAction(th)
            } finally {
                isDone = true
                finallyAction()
            }
        }
    }
}

@Composable
fun ProgressDialog() {
    Dialog(
        onDismissRequest = {},
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            shape = AlertDialogDefaults.shape,
            color = AlertDialogDefaults.containerColor,
            tonalElevation = AlertDialogDefaults.TonalElevation,
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()// LinearProgressIndicator()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProgressDialogPreview() {
    RubyCalcTheme {
        ProgressDialog()
    }
}
