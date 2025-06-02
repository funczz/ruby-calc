package com.github.funczz.ruby_calc.core.state

import com.github.funczz.kotlin.sam.SamStateNextAction
import com.github.funczz.ruby_calc.core.action.RubyCalcAction
import com.github.funczz.ruby_calc.core.model.RubyCalcStateData
import com.github.funczz.ruby_calc.core.model.RubyCalcStateModel
import com.github.funczz.ruby_calc.core.model.answer.AnswerDetails
import com.github.funczz.ruby_calc.core.model.answer.AnswerIndex
import com.github.funczz.ruby_calc.core.model.answer.AnswerStateData
import com.github.funczz.ruby_calc.core.model.element.ElementDetails
import com.github.funczz.ruby_calc.core.model.element.ElementStateData
import com.github.funczz.ruby_calc.core.model.problem.ProblemDetails
import com.github.funczz.ruby_calc.core.model.problem.ProblemSaveResult
import com.github.funczz.ruby_calc.core.model.problem.ProblemStateData
import com.github.funczz.ruby_calc.core.model.program.ProgramDetails
import com.github.funczz.ruby_calc.core.model.program.ProgramSaveResult
import com.github.funczz.ruby_calc.core.model.program.ProgramStateData
import com.github.funczz.ruby_calc.core.usecase.answer.DeleteAnswerUseCase
import com.github.funczz.ruby_calc.core.usecase.answer.GetListAnswerUseCase
import com.github.funczz.ruby_calc.core.usecase.answer.SaveAnswerUseCase
import com.github.funczz.ruby_calc.core.usecase.element.GetDetailsElementUseCase
import com.github.funczz.ruby_calc.core.usecase.element.SaveElementUseCase
import com.github.funczz.ruby_calc.core.usecase.problem.DeleteProblemUseCase
import com.github.funczz.ruby_calc.core.usecase.problem.GetDetailsProblemUseCase
import com.github.funczz.ruby_calc.core.usecase.problem.GetListProblemUseCase
import com.github.funczz.ruby_calc.core.usecase.problem.SaveProblemUseCase
import com.github.funczz.ruby_calc.core.usecase.program.DeleteProgramUseCase
import com.github.funczz.ruby_calc.core.usecase.program.GetDetailsProgramUseCase
import com.github.funczz.ruby_calc.core.usecase.program.GetListProgramUseCase
import com.github.funczz.ruby_calc.core.usecase.program.SaveProgramUseCase
import kotlin.jvm.optionals.getOrNull

@Suppress("Unused", "MemberVisibilityCanBePrivate")
object RubyCalcState : SamStateNextAction<RubyCalcStateModel> {

    fun isInitializedProgram(model: RubyCalcStateModel): Boolean {
        if (!model.stateData.isPresent) return false
        return when (val stateData = model.stateData.get()) {
            is ProgramStateData.InitializeData -> stateData.programDetails is ProgramDetails

            else -> false
        }
    }

    fun isOpenedProgram(model: RubyCalcStateModel): Boolean {
        if (!model.stateData.isPresent) return false
        return when (val stateData = model.stateData.get()) {
            is ProgramStateData.InputData -> stateData.value is GetDetailsProgramUseCase.InputData

            else -> false
        }
    }

    fun isUpdatedProgram(model: RubyCalcStateModel): Boolean {
        if (!model.stateData.isPresent) return false
        return when (val stateData = model.stateData.get()) {
            is ProgramStateData.InputData -> stateData.value is DeleteProgramUseCase.InputData
                    || stateData.value is SaveProgramUseCase.InputData

            else -> false
        }
    }

    fun isInitializedProblem(model: RubyCalcStateModel): Boolean {
        if (!model.stateData.isPresent) return false
        return when (val stateData = model.stateData.get()) {
            is ProblemStateData.InitializeData -> stateData.problemDetails is ProblemDetails

            else -> false
        }
    }

    fun isOpenedProblem(model: RubyCalcStateModel): Boolean {
        if (!model.stateData.isPresent) return false
        return when (val stateData = model.stateData.get()) {
            is ProblemStateData.InputData -> stateData.value is GetDetailsProblemUseCase.InputData

            else -> false
        }
    }

    fun isUpdatedProblem(model: RubyCalcStateModel): Boolean {
        if (!model.stateData.isPresent) return false
        return when (val stateData = model.stateData.get()) {
            is ProblemStateData.InputData -> stateData.value is DeleteProblemUseCase.InputData
                    || stateData.value is SaveProblemUseCase.InputData

            else -> false
        }
    }

    fun isUpdatedAnswer(model: RubyCalcStateModel): Boolean {
        if (!model.stateData.isPresent) return false
        return when (val stateData = model.stateData.get()) {
            is AnswerStateData.InputData -> stateData.value is SaveAnswerUseCase.InputData
                    || stateData.value is DeleteAnswerUseCase.InputData

            else -> false
        }
    }

    fun isUpdatedElement(model: RubyCalcStateModel): Boolean {
        if (!model.stateData.isPresent) return false
        return when (val stateData = model.stateData.get()) {
            is ElementStateData.InputData -> stateData.value is SaveElementUseCase.InputData
            else -> false
        }
    }

    override fun nextAction(model: RubyCalcStateModel): Boolean {
        return when {
            isInitializedProgram(model = model) -> {
                if (!model.programStateModel.programDetails.programModel.isPresent) {
                    val bulk = RubyCalcStateData.Bulk()
                    bulk.addStateData {
                        ProgramStateData.InitializeData(
                            programSaveResult = ProgramSaveResult(),
                        )
                    }
                    RubyCalcAction.accept(input = bulk, present = model::present)
                    true
                } else false
            }

            isOpenedProgram(model = model) -> {
                false
            }

            isUpdatedProgram(model = model) -> {
                val bulk = RubyCalcStateData.Bulk()
                bulk.addStateData {
                    ProgramStateData.InputData(
                        value = GetListProgramUseCase.InputData(
                            value = model.programStateModel.programIndex.value,
                            orderBy = model.programStateModel.programIndex.orderBy,
                            limit = model.programStateModel.programIndex.limit,
                        )
                    )
                }
                model.stateData.getOrNull()?.let { s ->
                    when (s) {
                        is ProgramStateData.InputData -> {
                            when (s.value) {
                                is SaveProgramUseCase.InputData -> {
                                    model.programStateModel.programSaveResult.id.getOrNull()
                                        ?.let { id ->
                                            bulk.addStateData {
                                                ProgramStateData.InputData(
                                                    value = GetDetailsProgramUseCase.IdInputData(id = id)
                                                )
                                            }
                                        }
                                }

                                is DeleteProgramUseCase.InputData -> {
                                    bulk.addStateData {
                                        ProgramStateData.InitializeData(
                                            programDetails = ProgramDetails()
                                        )
                                    }
                                }

                                else -> {}
                            }
                        }

                        else -> {}
                    }
                }
                RubyCalcAction.accept(input = bulk, present = model::present)
                true
            }

            isInitializedProblem(model = model) -> {
                if (!model.problemStateModel.problemDetails.problemModel.isPresent) {
                    val bulk = RubyCalcStateData.Bulk()
                    bulk.addStateData {
                        ProblemStateData.InitializeData(
                            problemSaveResult = ProblemSaveResult()
                        )
                    }
                    bulk.addStateData {
                        ElementStateData.InitializeData(
                            elementDetails = ElementDetails()
                        )
                    }
                    bulk.addStateData {
                        AnswerStateData.InitializeData(
                            answerDetails = AnswerDetails(),
                            answerIndex = AnswerIndex(
                                value = model.answerStateModel.answerIndex.value,
                                orderBy = model.answerStateModel.answerIndex.orderBy,
                                limit = model.answerStateModel.answerIndex.limit,
                            ),
                        )
                    }
                    RubyCalcAction.accept(input = bulk, present = model::present)
                    true
                } else false
            }

            isOpenedProblem(model = model) -> {
                model.problemStateModel.problemDetails.problemModel.getOrNull()?.let {
                    val bulk = RubyCalcStateData.Bulk()
                    bulk.addStateData {
                        ElementStateData.InputData(
                            value = GetDetailsElementUseCase.InputData(
                                problemId = it.id
                            )
                        )
                    }
                    bulk.addStateData {
                        AnswerStateData.InputData(
                            value = GetListAnswerUseCase.InputData(
                                problemId = it.id,
                                value = model.answerStateModel.answerIndex.value,
                                orderBy = model.answerStateModel.answerIndex.orderBy,
                                limit = model.answerStateModel.answerIndex.limit,
                            )
                        )
                    }
                    RubyCalcAction.accept(input = bulk, present = model::present)
                    true
                } ?: false
            }

            isUpdatedProblem(model = model) -> {
                val bulk = RubyCalcStateData.Bulk()
                bulk.addStateData {
                    ProblemStateData.InputData(
                        value = GetListProblemUseCase.InputData(
                            value = model.problemStateModel.problemIndex.value,
                            orderBy = model.problemStateModel.problemIndex.orderBy,
                            limit = model.problemStateModel.problemIndex.limit,
                        )
                    )
                }
                model.stateData.getOrNull()?.let { s ->
                    when (s) {
                        is ProblemStateData.InputData -> {
                            when (s.value) {
                                is SaveProblemUseCase.InputData -> {
                                    model.problemStateModel.problemSaveResult.id.getOrNull()
                                        ?.let { id ->
                                            bulk.addStateData {
                                                ProblemStateData.InputData(
                                                    value = GetDetailsProblemUseCase.IdInputData(id = id)
                                                )
                                            }
                                        }
                                }

                                is DeleteProblemUseCase.InputData -> {
                                    bulk.addStateData {
                                        ProblemStateData.InitializeData(
                                            problemDetails = ProblemDetails()
                                        )
                                    }
                                }

                                else -> {}
                            }
                        }

                        else -> {}
                    }
                }
                RubyCalcAction.accept(input = bulk, present = model::present)
                true
            }

            isUpdatedElement(model = model) -> {
                model.problemStateModel.problemDetails.problemModel.getOrNull()?.let { m ->
                    val bulk = RubyCalcStateData.Bulk()
                    bulk.addStateData {
                        ElementStateData.InputData(
                            value = GetDetailsElementUseCase.InputData(
                                problemId = m.id,
                            )
                        )
                    }
                    bulk.addStateData {
                        ProblemStateData.InputData(
                            value = GetDetailsProblemUseCase.IdInputData(id = m.id)
                        )
                    }
                    RubyCalcAction.accept(input = bulk, present = model::present)
                    true
                } ?: false
            }

            isUpdatedAnswer(model = model) -> {
                model.problemStateModel.problemDetails.problemModel.getOrNull()?.let { m ->
                    val bulk = RubyCalcStateData.Bulk()
                    bulk.addStateData {
                        AnswerStateData.InputData(
                            value = GetListAnswerUseCase.InputData(
                                problemId = m.id,
                                value = model.answerStateModel.answerIndex.value,
                                orderBy = model.answerStateModel.answerIndex.orderBy,
                                limit = model.answerStateModel.answerIndex.limit,
                            )
                        )
                    }
                    bulk.addStateData {
                        ProblemStateData.InputData(
                            value = GetDetailsProblemUseCase.IdInputData(id = m.id)
                        )
                    }
                    RubyCalcAction.accept(input = bulk, present = model::present)
                    true
                } ?: false
            }

            else -> false
        }
    }

}
