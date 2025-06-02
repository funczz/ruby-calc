package com.github.funczz.ruby_calc.core.model.error

import com.github.funczz.kotlin.logging.LoggerFactory
import com.github.funczz.kotlin.sam.SamModel

@Suppress("Unused", "MemberVisibilityCanBePrivate")
class ErrorStateModel : SamModel<ErrorStateData> {

    val throwableList: List<Throwable> get() = _throwableList.toList()

    private val _throwableList = mutableListOf<Throwable>()

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun present(data: ErrorStateData) {

        when (data) {
            is ErrorStateData.InitializeData -> {
                logger.info { "ErrorStateData.InitializeData=%s".format(data.toString()) }
                _throwableList.clear()
                _throwableList.addAll(data.throwableList)
            }

            is ErrorStateData.ThrowableData -> {
                logger.info { "ErrorStateData.ThrowableData=%s".format(data.toString()) }
                _throwableList.add(data.throwable)
            }

            is ErrorStateData.FunctionData -> {
                logger.info { "ErrorStateData.FunctionData=%s".format(data.toString()) }
                try {
                    data.function()
                    logger.info { "ErrorStateData.FunctionData: SUCCESS" }
                } catch (th: Throwable) {
                    logger.severe { th.stackTraceToString() }
                    _throwableList.add(th)
                }
            }
        }
    }

    override fun toString(): String {
        return "ErrorStateModel(throwableList=%s)".format(throwableList.joinToString(", ") { it.toString() })
    }


    companion object {

        fun new(): ErrorStateModel = ErrorStateModel()
    }
}
