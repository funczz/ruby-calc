package com.github.funczz.ruby_calc.android

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import com.github.funczz.kotlin.logging.LoggerFactory
import com.github.funczz.kotlin.undo.UndoManager

class TextFieldState(
    text: String,
    private val undoManager: UndoManager<TextFieldValue>? = UndoManager(TextFieldValue(text = text))
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    var value: TextFieldValue by mutableStateOf(TextFieldValue(text = text))
        private set

    fun onValueChange(value: TextFieldValue) {
        logger.info { "text=%s, value=%s".format(value.text, value.toString()) }
        if (value.text != this.value.text) {
            logger.info {
                "Change: new text=%s, old value=%s".format(value.text, this.value.text)
            }
            undoManager?.change(item = value)
        }
        this.value = value
        logger.info {
            "[UNDO]\n%s\n[REDO]\n%s".format(
                undoManager?.undoItems()?.joinToString("\n"),
                undoManager?.redoItems()?.joinToString("\n"),
            )
        }
    }

    fun canUndo(): Boolean = undoManager?.canUndo() ?: false

    fun canRedo(): Boolean = undoManager?.canRedo() ?: false

    fun undo() {
        if (undoManager == null || !undoManager.canUndo()) return
        value = undoManager.undo()
        logger.info {
            "[UNDO]\n%s\n[REDO]\n%s".format(
                undoManager.undoItems().joinToString("\n"),
                undoManager.redoItems().joinToString("\n"),
            )
        }
    }

    fun redo() {
        if (undoManager == null || !undoManager.canRedo()) return
        value = undoManager.redo()
        logger.info {
            "[UNDO]\n%s\n[REDO]\n%s".format(
                undoManager.undoItems().joinToString("\n"),
                undoManager.redoItems().joinToString("\n"),
            )
        }
    }
}
