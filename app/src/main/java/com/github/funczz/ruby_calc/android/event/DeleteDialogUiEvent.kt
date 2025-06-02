package com.github.funczz.ruby_calc.android.event


import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.github.funczz.ruby_calc.android.R

@Suppress("Unused")
data class DeleteDialogUiEvent(
    val confirmAction: () -> Unit,
    val dismissAction: () -> Unit,
) : UiEvent {

    override var isDone: Boolean = false

    @Composable
    fun Show() {
        if (isDone) return
        AlertDialog(
            onDismissRequest = { //画面の外側がタップされたら
            },
            title = { Text(text = stringResource(R.string.delete_dialog_title)) },
            text = { Text(text = stringResource(R.string.delete_dialog_text)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        isDone = true
                        confirmAction()
                    }
                ) {
                    Text(text = stringResource(R.string.delete_dialog_confirm))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        isDone = true
                        dismissAction()
                    }
                ) {
                    Text(text = stringResource(R.string.delete_dialog_dismiss))
                }
            }
        )
    }

}
