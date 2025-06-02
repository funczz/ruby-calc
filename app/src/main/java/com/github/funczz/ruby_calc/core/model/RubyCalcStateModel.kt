package com.github.funczz.ruby_calc.core.model

import com.github.funczz.kotlin.logging.LoggerFactory
import com.github.funczz.kotlin.sam.SamModel
import com.github.funczz.ruby_calc.core.model.answer.AnswerStateData
import com.github.funczz.ruby_calc.core.model.answer.AnswerStateModel
import com.github.funczz.ruby_calc.core.model.element.ElementStateData
import com.github.funczz.ruby_calc.core.model.element.ElementStateModel
import com.github.funczz.ruby_calc.core.model.error.ErrorStateData
import com.github.funczz.ruby_calc.core.model.error.ErrorStateModel
import com.github.funczz.ruby_calc.core.model.problem.ProblemStateData
import com.github.funczz.ruby_calc.core.model.problem.ProblemStateModel
import com.github.funczz.ruby_calc.core.model.program.ProgramStateData
import com.github.funczz.ruby_calc.core.model.program.ProgramStateModel
import com.github.funczz.ruby_calc.core.model.ruby.RubyStateData
import com.github.funczz.ruby_calc.core.model.ruby.RubyStateModel
import com.github.funczz.ruby_calc.core.state.RubyCalcState
import java.util.Optional

@Suppress("Unused", "MemberVisibilityCanBePrivate")
class RubyCalcStateModel(

    val programStateModel: ProgramStateModel,

    val problemStateModel: ProblemStateModel,

    val elementStateModel: ElementStateModel,

    val answerStateModel: AnswerStateModel,

    val rubyStateModel: RubyStateModel,

    val errorStateModel: ErrorStateModel,

    ) : SamModel<RubyCalcStateData> {

    var stateData: Optional<RubyCalcStateData> = Optional.empty()
        private set

    private val logger = LoggerFactory.getLogger(this::class.java)

    private fun presentPerformed(data: RubyCalcStateData) {
        try {
            when (data) {
                is ProgramStateData -> programStateModel.present(data = data)
                is ProblemStateData -> problemStateModel.present(data = data)
                is ElementStateData -> elementStateModel.present(data = data)
                is AnswerStateData -> answerStateModel.present(data = data)
                is RubyStateData -> rubyStateModel.present(data = data)
                is ErrorStateData -> errorStateModel.present(data = data)
            }
        } catch (th: Throwable) {
            errorStateModel.present(data = ErrorStateData.ThrowableData(throwable = th))
            logger.severe { th.stackTraceToString() }
            //throw th
        }
        stateData = Optional.ofNullable(data)

        /** 次アクションを適用 */
        while (true) {
            if (!RubyCalcState.nextAction(model = this)) break
        }
    }

    override fun present(data: RubyCalcStateData) {
        when (data) {
            is RubyCalcStateData.Bulk -> {
                for (v in data.values) {
                    presentPerformed(data = v)
                }
            }

            else -> presentPerformed(data = data)
        }
    }

    override fun toString(): String {
        return """RubyCalcStateModel(
            |  programStateModel=%s
            |  problemStateModel=%s
            |  elementStateModel=%s
            |  answerStateModel=%s
            |  rubyStateModel=%s
            |  errorStateModel=%s
            |  stateData=%s
            |)""".trimMargin("|").format(
            programStateModel,
            problemStateModel,
            elementStateModel,
            answerStateModel,
            rubyStateModel,
            errorStateModel,
            stateData,
        )
    }
}
