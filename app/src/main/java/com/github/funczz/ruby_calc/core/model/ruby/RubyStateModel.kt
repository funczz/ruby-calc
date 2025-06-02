package com.github.funczz.ruby_calc.core.model.ruby

import com.github.funczz.kotlin.logging.LoggerFactory
import com.github.funczz.kotlin.sam.SamModel
import com.github.funczz.ruby_calc.core.interactor.ruby.ExecuteRubyInteractor
import com.github.funczz.ruby_calc.core.service.RubyService
import com.github.funczz.ruby_calc.core.usecase.ruby.ExecuteRubyUseCase
import java.util.Optional

@Suppress("Unused", "MemberVisibilityCanBePrivate")
class RubyStateModel(

    private val executeRubyUseCase: ExecuteRubyUseCase

) : SamModel<RubyStateData> {

    var rubyResult: RubyResult = RubyResult()
        private set

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun present(data: RubyStateData) {
        rubyResult = when (data) {
            is RubyStateData.InitializeData -> data.rubyResult ?: rubyResult

            is RubyStateData.InputData -> {
                when (val v = data.value) {
                    is ExecuteRubyUseCase.InputData -> {
                        logger.info { "ExecuteRubyUseCase.InputData=%s".format(v.toString()) }
                        try {
                            RubyResult(success = executeRubyUseCase(inputData = v))
                        } catch (th: Throwable) {
                            logger.warning { th.stackTraceToString() }
                            RubyResult(failure = Optional.of(th))
                        }
                    }

                    else -> {
                        val message = "Invalid argument type: data=%s".format(v.toString())
                        logger.severe { message }
                        throw IllegalArgumentException(message)
                    }
                }
            }
        }
    }

    override fun toString(): String {
        return "RubyStateModel(rubyResult=%s)".format(rubyResult)
    }

    companion object {

        fun new(
            rubyService: RubyService,
        ): RubyStateModel = RubyStateModel(
            executeRubyUseCase = ExecuteRubyInteractor(rubyService = rubyService)
        )
    }
}
