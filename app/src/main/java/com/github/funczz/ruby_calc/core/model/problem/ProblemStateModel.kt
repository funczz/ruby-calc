package com.github.funczz.ruby_calc.core.model.problem

import com.github.funczz.kotlin.logging.LoggerFactory
import com.github.funczz.kotlin.sam.SamModel
import com.github.funczz.ruby_calc.core.data.provider.answer.FindAnswerDataProvider
import com.github.funczz.ruby_calc.core.data.provider.element.RWElementDataProvider
import com.github.funczz.ruby_calc.core.data.provider.problem.CRUDProblemDataProvider
import com.github.funczz.ruby_calc.core.data.provider.problem.FindProblemDataProvider
import com.github.funczz.ruby_calc.core.data.provider.program.CRUDProgramDataProvider
import com.github.funczz.ruby_calc.core.data.provider.setting.RWSettingDataProvider
import com.github.funczz.ruby_calc.core.interactor.answer.GetListAnswerInteractor
import com.github.funczz.ruby_calc.core.interactor.element.GetDetailsElementInteractor
import com.github.funczz.ruby_calc.core.interactor.problem.DeleteProblemInteractor
import com.github.funczz.ruby_calc.core.interactor.problem.GetDetailsProblemInteractor
import com.github.funczz.ruby_calc.core.interactor.problem.GetListProblemInteractor
import com.github.funczz.ruby_calc.core.interactor.problem.SaveProblemInteractor
import com.github.funczz.ruby_calc.core.interactor.program.GetDetailsProgramInteractor
import com.github.funczz.ruby_calc.core.interactor.setting.LoadSettingInteractor
import com.github.funczz.ruby_calc.core.interactor.setting.ProblemDetailsLoadSettingInteractor
import com.github.funczz.ruby_calc.core.interactor.setting.ProblemDetailsSaveSettingInteractor
import com.github.funczz.ruby_calc.core.interactor.setting.ProblemEditLoadSettingInteractor
import com.github.funczz.ruby_calc.core.interactor.setting.ProblemEditSaveSettingInteractor
import com.github.funczz.ruby_calc.core.interactor.setting.ProblemIndexLoadSettingInteractor
import com.github.funczz.ruby_calc.core.interactor.setting.ProblemIndexSaveSettingInteractor
import com.github.funczz.ruby_calc.core.interactor.setting.SaveSettingInteractor
import com.github.funczz.ruby_calc.core.usecase.answer.GetListAnswerUseCase
import com.github.funczz.ruby_calc.core.usecase.element.GetDetailsElementUseCase
import com.github.funczz.ruby_calc.core.usecase.problem.DeleteProblemUseCase
import com.github.funczz.ruby_calc.core.usecase.problem.GetDetailsProblemUseCase
import com.github.funczz.ruby_calc.core.usecase.problem.GetListProblemUseCase
import com.github.funczz.ruby_calc.core.usecase.problem.SaveProblemUseCase
import com.github.funczz.ruby_calc.core.usecase.program.GetDetailsProgramUseCase
import com.github.funczz.ruby_calc.core.usecase.setting.ProblemDetailsLoadSettingUseCase
import com.github.funczz.ruby_calc.core.usecase.setting.ProblemDetailsSaveSettingUseCase
import com.github.funczz.ruby_calc.core.usecase.setting.ProblemEditLoadSettingUseCase
import com.github.funczz.ruby_calc.core.usecase.setting.ProblemEditSaveSettingUseCase
import com.github.funczz.ruby_calc.core.usecase.setting.ProblemIndexLoadSettingUseCase
import com.github.funczz.ruby_calc.core.usecase.setting.ProblemIndexSaveSettingUseCase
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

@Suppress("Unused", "MemberVisibilityCanBePrivate")
class ProblemStateModel(

    private val deleteProblemUseCase: DeleteProblemUseCase,

    private val getDetailsProblemUseCase: GetDetailsProblemUseCase,

    private val getListProblemUseCase: GetListProblemUseCase,

    private val saveProblemUseCase: SaveProblemUseCase,

    private val getDetailsProgramUseCase: GetDetailsProgramUseCase,

    private val getDetailsElementUseCase: GetDetailsElementUseCase,

    private val getListAnswerUseCase: GetListAnswerUseCase,

    private val problemIndexLoadSettingUseCase: ProblemIndexLoadSettingUseCase,

    private val problemIndexSaveSettingUseCase: ProblemIndexSaveSettingUseCase,

    private val problemDetailsLoadSettingUseCase: ProblemDetailsLoadSettingUseCase,

    private val problemDetailsSaveSettingUseCase: ProblemDetailsSaveSettingUseCase,

    private val problemEditLoadSettingUseCase: ProblemEditLoadSettingUseCase,

    private val problemEditSaveSettingUseCase: ProblemEditSaveSettingUseCase,

    problemDetails: ProblemDetails = ProblemDetails(),

    problemIndex: ProblemIndex = ProblemIndex(),

    problemSaveResult: ProblemSaveResult = ProblemSaveResult(),

    problemEdit: ProblemEdit = ProblemEdit(),

    ) : SamModel<ProblemStateData> {

    var problemDetails: ProblemDetails = problemDetails
        private set

    var problemIndex: ProblemIndex = problemIndex
        private set

    var problemSaveResult: ProblemSaveResult = problemSaveResult
        private set

    var problemEdit: ProblemEdit = problemEdit
        private set

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun present(data: ProblemStateData) {
        when (data) {
            is ProblemStateData.InitializeData -> {
                logger.info { "ProblemStateData.InitializeData=%s".format(data.toString()) }
                data.problemDetails?.let { problemDetails = it }
                data.problemIndex?.let { problemIndex = it }
                data.problemSaveResult?.let { problemSaveResult = it }
                data.problemEdit?.let { problemEdit = it }
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

            is ProblemStateData.LoadData -> {
                //Load ProblemIndex
                val problemIndex = problemIndexLoadSettingUseCase(
                    inputData = ProblemIndexLoadSettingUseCase.InputData(
                        problemIndex = problemIndex
                    )
                )
                val problemIndexInputData = ProblemStateData.InputData(
                    value = GetListProblemUseCase.InputData(
                        value = problemIndex.value,
                        orderColumn = problemIndex.orderColumn,
                        orderBy = problemIndex.orderBy,
                        limit = problemIndex.limit,
                    )
                )
                present(data = problemIndexInputData)

                //Load ProblemDetails
                problemDetailsLoadSettingUseCase(
                    inputData = ProblemDetailsLoadSettingUseCase.InputData(
                        defaultId = Optional.empty()
                    )
                ).getOrNull()?.let {
                    val problemDetailsIndexData = ProblemStateData.InputData(
                        value = GetDetailsProblemUseCase.IdInputData(id = it)
                    )
                    present(data = problemDetailsIndexData)
                }

                //Load ProblemEdit
                var defaultProblemEdit = when (problemDetails.problemModel.isPresent) {
                    true -> {
                        val model = problemDetails.problemModel.get()
                        ProblemEdit(
                            id = model.id,
                            name = model.name,
                            comment = model.comment,
                        )
                    }

                    else -> ProblemEdit()
                }
                defaultProblemEdit = when (problemDetails.programModel.isPresent) {
                    true -> {
                        val model = problemDetails.programModel.get()
                        defaultProblemEdit.copy(
                            programId = model.id,
                            programName = model.name,
                            programDescription = model.description,
                            programHint = model.hint,
                            programCode = model.code,
                            programCreatedAt = model.createdAt,
                            programUpdatedAt = model.updatedAt,
                        )
                    }

                    else -> defaultProblemEdit
                }
                val problemEditData = problemEditLoadSettingUseCase(
                    inputData = ProblemEditLoadSettingUseCase.InputData(
                        problemEdit = defaultProblemEdit
                    )
                )
                val problemInitializeData = ProblemStateData.InitializeData(
                    problemEdit = problemEditData
                )
                present(data = problemInitializeData)
            }

            is ProblemStateData.SaveData -> {
                //Save ProblemIndex
                problemIndexSaveSettingUseCase(
                    inputData = ProblemIndexSaveSettingUseCase.InputData(
                        problemIndex = problemIndex
                    )
                )

                //Save ProblemDetails
                problemDetailsSaveSettingUseCase(
                    inputData = ProblemDetailsSaveSettingUseCase.InputData(
                        problemId = Optional.ofNullable(problemDetails.problemModel.getOrNull()?.id)
                    )
                )

                //Save ProblemEdit
                problemEditSaveSettingUseCase(
                    inputData = ProblemEditSaveSettingUseCase.InputData(
                        problemEdit = problemEdit
                    )
                )
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
            rwSettingDataProvider: RWSettingDataProvider,
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
            problemIndexLoadSettingUseCase = ProblemIndexLoadSettingInteractor(
                useCase = LoadSettingInteractor(provider = rwSettingDataProvider)
            ),
            problemIndexSaveSettingUseCase = ProblemIndexSaveSettingInteractor(
                useCase = SaveSettingInteractor(provider = rwSettingDataProvider)
            ),
            problemDetailsLoadSettingUseCase = ProblemDetailsLoadSettingInteractor(
                useCase = LoadSettingInteractor(provider = rwSettingDataProvider)
            ),
            problemDetailsSaveSettingUseCase = ProblemDetailsSaveSettingInteractor(
                useCase = SaveSettingInteractor(provider = rwSettingDataProvider)
            ),
            problemEditLoadSettingUseCase = ProblemEditLoadSettingInteractor(
                useCase = LoadSettingInteractor(provider = rwSettingDataProvider)
            ),
            problemEditSaveSettingUseCase = ProblemEditSaveSettingInteractor(
                useCase = SaveSettingInteractor(provider = rwSettingDataProvider)
            )
        )
    }
}
