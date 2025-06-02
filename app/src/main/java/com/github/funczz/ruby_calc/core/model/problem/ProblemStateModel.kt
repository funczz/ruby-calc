package com.github.funczz.ruby_calc.core.model.problem

import com.github.funczz.kotlin.logging.LoggerFactory
import com.github.funczz.kotlin.sam.SamModel
import com.github.funczz.ruby_calc.core.data.provider.answer.FindAnswerDataProvider
import com.github.funczz.ruby_calc.core.data.provider.element.RWElementDataProvider
import com.github.funczz.ruby_calc.core.data.provider.problem.CRUDProblemDataProvider
import com.github.funczz.ruby_calc.core.data.provider.problem.FindProblemDataProvider
import com.github.funczz.ruby_calc.core.data.provider.program.CRUDProgramDataProvider
import com.github.funczz.ruby_calc.core.interactor.answer.GetListAnswerInteractor
import com.github.funczz.ruby_calc.core.interactor.element.GetDetailsElementInteractor
import com.github.funczz.ruby_calc.core.interactor.problem.DeleteProblemInteractor
import com.github.funczz.ruby_calc.core.interactor.problem.GetDetailsProblemInteractor
import com.github.funczz.ruby_calc.core.interactor.problem.GetListProblemInteractor
import com.github.funczz.ruby_calc.core.interactor.problem.SaveProblemInteractor
import com.github.funczz.ruby_calc.core.interactor.program.GetDetailsProgramInteractor
import com.github.funczz.ruby_calc.core.usecase.answer.GetListAnswerUseCase
import com.github.funczz.ruby_calc.core.usecase.element.GetDetailsElementUseCase
import com.github.funczz.ruby_calc.core.usecase.problem.DeleteProblemUseCase
import com.github.funczz.ruby_calc.core.usecase.problem.GetDetailsProblemUseCase
import com.github.funczz.ruby_calc.core.usecase.problem.GetListProblemUseCase
import com.github.funczz.ruby_calc.core.usecase.problem.SaveProblemUseCase
import com.github.funczz.ruby_calc.core.usecase.program.GetDetailsProgramUseCase
import java.util.Optional

@Suppress("Unused", "MemberVisibilityCanBePrivate")
class ProblemStateModel(

    private val deleteProblemUseCase: DeleteProblemUseCase,

    private val getDetailsProblemUseCase: GetDetailsProblemUseCase,

    private val getListProblemUseCase: GetListProblemUseCase,

    private val saveProblemUseCase: SaveProblemUseCase,

    private val getDetailsProgramUseCase: GetDetailsProgramUseCase,

    private val getDetailsElementUseCase: GetDetailsElementUseCase,

    private val getListAnswerUseCase: GetListAnswerUseCase,

    problemDetails: ProblemDetails = ProblemDetails(),

    problemIndex: ProblemIndex = ProblemIndex(),

    problemSaveResult: ProblemSaveResult = ProblemSaveResult(),

    ) : SamModel<ProblemStateData> {

    var problemDetails: ProblemDetails = problemDetails
        private set

    var problemIndex: ProblemIndex = problemIndex
        private set

    var problemSaveResult: ProblemSaveResult = problemSaveResult
        private set

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun present(data: ProblemStateData) {
        when (data) {
            is ProblemStateData.InitializeData -> {
                logger.info { "ProblemStateData.InitializeData=%s".format(data.toString()) }
                data.problemDetails?.let { problemDetails = it }
                data.problemIndex?.let { problemIndex = it }
                data.problemSaveResult?.let { problemSaveResult = it }
            }

            is ProblemStateData.InputData -> {
                when (val v = data.value) {
                    is DeleteProblemUseCase.InputData -> {
                        logger.info { "DeleteProblemUseCase.InputData=%s".format(v.toString()) }
                        deleteProblemUseCase(inputData = v)
                    }

                    is GetDetailsProblemUseCase.InputData -> {
                        logger.info { "GetDetailsProblemUseCase.InputData=%s".format(v.toString()) }
                        val problemModel = getDetailsProblemUseCase(inputData = v)
                        val programModel = when (problemModel.isPresent) {
                            true -> getDetailsProgramUseCase(
                                inputData = GetDetailsProgramUseCase.IdInputData(id = problemModel.get().programId)
                            )

                            else -> Optional.empty()
                        }
                        val canBeDeleted = when (problemModel.isPresent) {
                            true -> getDetailsElementUseCase(
                                inputData = GetDetailsElementUseCase.InputData(problemId = problemModel.get().id)
                            ).isEmpty() && getListAnswerUseCase(
                                inputData = GetListAnswerUseCase.InputData(
                                    problemId = problemModel.get().id,
                                    limit = 1,
                                )
                            ).isEmpty()

                            else -> false
                        }
                        problemDetails = ProblemDetails(
                            problemModel = problemModel,
                            programModel = programModel,
                            canBeDeleted = canBeDeleted,
                        )
                    }

                    is GetListProblemUseCase.InputData -> {
                        logger.info { "GetListProblemUseCase.InputData=%s".format(v.toString()) }
                        problemIndex = ProblemIndex(
                            problemList = getListProblemUseCase(inputData = v),
                            programId = v.programId,
                            value = v.value,
                            orderColumn = v.orderColumn,
                            orderBy = v.orderBy,
                            limit = v.limit,
                        )
                    }

                    is SaveProblemUseCase.InputData -> {
                        logger.info { "SaveProblemUseCase.InputData=%s".format(v.toString()) }
                        val getListInputData = GetListProblemUseCase.InputData(
                            value = v.name,
                            limit = 1
                        )
                        val modelList = getListProblemUseCase(getListInputData)
                        problemSaveResult = when (modelList.isNotEmpty()) {
                            true -> {
                                val model = modelList.first()
                                when {
                                    v.id is Long && v.id == model.id -> ProblemSaveResult()
                                    else -> ProblemSaveResult(existsName = true)
                                }
                            }

                            else -> ProblemSaveResult()
                        }

                        if (!problemSaveResult.existsName) {
                            saveProblemUseCase(inputData = v)
                            problemSaveResult = ProblemSaveResult(
                                id = Optional.ofNullable(getListProblemUseCase(getListInputData).firstOrNull()?.id)
                            )
                        }
                    }
                }
            }
        }
    }

    override fun toString(): String {
        return "ProblemStateModel(problemDetails=%s, problemIndex=%s, problemSaveResult=%s)".format(
            problemDetails,
            problemIndex,
            problemSaveResult
        )
    }

    companion object {

        fun new(
            crudProblemDataProvider: CRUDProblemDataProvider,
            crudProgramDataProvider: CRUDProgramDataProvider,
            findProblemDataProvider: FindProblemDataProvider,
            rwElementDataProvider: RWElementDataProvider,
            findAnswerDataProvider: FindAnswerDataProvider,
        ): ProblemStateModel = ProblemStateModel(
            deleteProblemUseCase = DeleteProblemInteractor(provider = crudProblemDataProvider),
            getDetailsProblemUseCase = GetDetailsProblemInteractor(provider = crudProblemDataProvider),
            getListProblemUseCase = GetListProblemInteractor(provider = findProblemDataProvider),
            saveProblemUseCase = SaveProblemInteractor(provider = crudProblemDataProvider),
            getDetailsProgramUseCase = GetDetailsProgramInteractor(provider = crudProgramDataProvider),
            getDetailsElementUseCase = GetDetailsElementInteractor(provider = rwElementDataProvider),
            getListAnswerUseCase = GetListAnswerInteractor(provider = findAnswerDataProvider),
        )
    }
}
