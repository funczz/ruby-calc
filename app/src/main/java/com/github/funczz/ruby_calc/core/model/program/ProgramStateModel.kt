package com.github.funczz.ruby_calc.core.model.program

import com.github.funczz.kotlin.logging.LoggerFactory
import com.github.funczz.kotlin.sam.SamModel
import com.github.funczz.ruby_calc.core.data.provider.problem.FindProblemDataProvider
import com.github.funczz.ruby_calc.core.data.provider.program.CRUDProgramDataProvider
import com.github.funczz.ruby_calc.core.data.provider.program.FindProgramDataProvider
import com.github.funczz.ruby_calc.core.data.provider.setting.RWSettingDataProvider
import com.github.funczz.ruby_calc.core.interactor.problem.GetListProblemInteractor
import com.github.funczz.ruby_calc.core.interactor.program.DeleteProgramInteractor
import com.github.funczz.ruby_calc.core.interactor.program.GetDetailsProgramInteractor
import com.github.funczz.ruby_calc.core.interactor.program.GetListProgramInteractor
import com.github.funczz.ruby_calc.core.interactor.program.SaveProgramInteractor
import com.github.funczz.ruby_calc.core.interactor.setting.LoadSettingInteractor
import com.github.funczz.ruby_calc.core.interactor.setting.ProgramDetailsLoadSettingInteractor
import com.github.funczz.ruby_calc.core.interactor.setting.ProgramDetailsSaveSettingInteractor
import com.github.funczz.ruby_calc.core.interactor.setting.ProgramEditLoadSettingInteractor
import com.github.funczz.ruby_calc.core.interactor.setting.ProgramEditSaveSettingInteractor
import com.github.funczz.ruby_calc.core.interactor.setting.ProgramIndexLoadSettingInteractor
import com.github.funczz.ruby_calc.core.interactor.setting.ProgramIndexSaveSettingInteractor
import com.github.funczz.ruby_calc.core.interactor.setting.SaveSettingInteractor
import com.github.funczz.ruby_calc.core.usecase.problem.GetListProblemUseCase
import com.github.funczz.ruby_calc.core.usecase.program.DeleteProgramUseCase
import com.github.funczz.ruby_calc.core.usecase.program.GetDetailsProgramUseCase
import com.github.funczz.ruby_calc.core.usecase.program.GetListProgramUseCase
import com.github.funczz.ruby_calc.core.usecase.program.SaveProgramUseCase
import com.github.funczz.ruby_calc.core.usecase.setting.ProgramDetailsLoadSettingUseCase
import com.github.funczz.ruby_calc.core.usecase.setting.ProgramDetailsSaveSettingUseCase
import com.github.funczz.ruby_calc.core.usecase.setting.ProgramEditLoadSettingUseCase
import com.github.funczz.ruby_calc.core.usecase.setting.ProgramEditSaveSettingUseCase
import com.github.funczz.ruby_calc.core.usecase.setting.ProgramIndexLoadSettingUseCase
import com.github.funczz.ruby_calc.core.usecase.setting.ProgramIndexSaveSettingUseCase
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

@Suppress("Unused", "MemberVisibilityCanBePrivate")
class ProgramStateModel(

    private val deleteProgramUseCase: DeleteProgramUseCase,

    private val getDetailsProgramUseCase: GetDetailsProgramUseCase,

    private val getListProgramUseCase: GetListProgramUseCase,

    private val saveProgramUseCase: SaveProgramUseCase,

    private val getListProblemUseCase: GetListProblemUseCase,

    private val programIndexLoadSettingUseCase: ProgramIndexLoadSettingUseCase,

    private val programIndexSaveSettingUseCase: ProgramIndexSaveSettingUseCase,

    private val programDetailsLoadSettingUseCase: ProgramDetailsLoadSettingUseCase,

    private val programDetailsSaveSettingUseCase: ProgramDetailsSaveSettingUseCase,

    private val programEditLoadSettingUseCase: ProgramEditLoadSettingUseCase,

    private val programEditSaveSettingUseCase: ProgramEditSaveSettingUseCase,

    programDetails: ProgramDetails = ProgramDetails(),

    programIndex: ProgramIndex = ProgramIndex(),

    programSaveResult: ProgramSaveResult = ProgramSaveResult(),

    programSelection: ProgramIndex = ProgramIndex(),

    programEdit: ProgramEdit = ProgramEdit(),

    ) : SamModel<ProgramStateData> {

    var programDetails: ProgramDetails = programDetails
        private set

    var programIndex: ProgramIndex = programIndex
        private set

    var programSaveResult: ProgramSaveResult = programSaveResult
        private set

    var programSelection: ProgramIndex = programSelection
        private set

    var programEdit: ProgramEdit = programEdit
        private set

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun present(data: ProgramStateData) {
        when (data) {
            is ProgramStateData.InitializeData -> {
                logger.info { "ProgramStateData.InitializeData=%s".format(data.toString()) }
                data.programDetails?.let { programDetails = it }
                data.programIndex?.let { programIndex = it }
                data.programSaveResult?.let { programSaveResult = it }
                data.programSelection?.let { programSelection = it }
                data.programEdit?.let { programEdit = it }
            }

            is ProgramStateData.SelectionData -> {
                logger.info { "ProgramStateData.SelectionData=%s".format(data.toString()) }
                programSelection = ProgramIndex(
                    programList = getListProgramUseCase(inputData = data.value),
                    value = data.value.value,
                    orderColumn = data.value.orderColumn,
                    orderBy = data.value.orderBy,
                    limit = data.value.limit,
                )
            }

            is ProgramStateData.InputData -> {
                when (val v = data.value) {
                    is DeleteProgramUseCase.InputData -> {
                        logger.info { "DeleteProgramUseCase.InputData=%s".format(v.toString()) }
                        deleteProgramUseCase(inputData = v)
                    }

                    is GetDetailsProgramUseCase.InputData -> {
                        logger.info { "GetDetailsProgramUseCase.InputData=%s".format(v.toString()) }
                        val programModel = getDetailsProgramUseCase(inputData = v)
                        val canBeDeleted = when (programModel.isPresent) {
                            true -> {
                                getListProblemUseCase(
                                    inputData = GetListProblemUseCase.InputData(
                                        programId = programModel.get().id,
                                        limit = 1,
                                    )
                                ).isEmpty()
                            }

                            else -> false
                        }
                        programDetails = ProgramDetails(
                            programModel = programModel,
                            canBeDeleted = canBeDeleted,
                        )
                    }

                    is GetListProgramUseCase.InputData -> {
                        logger.info { "GetListProgramUseCase.InputData=%s".format(v.toString()) }
                        programIndex = ProgramIndex(
                            programList = getListProgramUseCase(inputData = v),
                            value = v.value,
                            orderColumn = v.orderColumn,
                            orderBy = v.orderBy,
                            limit = v.limit,
                        )
                    }

                    is SaveProgramUseCase.InputData -> {
                        logger.info { "SaveProgramUseCase.InputData=%s".format(v.toString()) }
                        val getListInputData = GetListProgramUseCase.InputData(
                            value = v.name,
                            limit = 1
                        )
                        val modelList = getListProgramUseCase(getListInputData)
                        programSaveResult = when (modelList.isNotEmpty()) {
                            true -> {
                                val model = modelList.first()
                                when {
                                    v.id is Long && v.id == model.id -> ProgramSaveResult()
                                    else -> ProgramSaveResult(existsName = true)
                                }
                            }

                            else -> ProgramSaveResult()
                        }

                        if (!programSaveResult.existsName) {
                            saveProgramUseCase(inputData = v)
                            programSaveResult = ProgramSaveResult(
                                id = Optional.ofNullable(getListProgramUseCase(getListInputData).firstOrNull()?.id)
                            )
                        }
                    }
                }
            }

            is ProgramStateData.LoadData -> {
                //Load ProgramIndex
                val programIndex = programIndexLoadSettingUseCase(
                    inputData = ProgramIndexLoadSettingUseCase.InputData(
                        programIndex = programIndex
                    )
                )
                val programIndexInputData = ProgramStateData.InputData(
                    value = GetListProgramUseCase.InputData(
                        value = programIndex.value,
                        orderColumn = programIndex.orderColumn,
                        orderBy = programIndex.orderBy,
                        limit = programIndex.limit,
                    )
                )
                present(data = programIndexInputData)

                //Load ProgramDetails
                programDetailsLoadSettingUseCase(
                    inputData = ProgramDetailsLoadSettingUseCase.InputData(
                        defaultId = Optional.empty()
                    )
                ).getOrNull()?.let {
                    val programDetailsIndexData = ProgramStateData.InputData(
                        value = GetDetailsProgramUseCase.IdInputData(id = it)
                    )
                    present(data = programDetailsIndexData)
                }

                //Load ProgramEdit
                val defaultProgramEdit = when (programDetails.programModel.isPresent) {
                    true -> {
                        val model = programDetails.programModel.get()
                        ProgramEdit(
                            id = model.id,
                            name = model.name,
                            description = model.description,
                            hint = model.hint,
                            code = model.code,
                        )
                    }

                    else -> ProgramEdit()
                }
                val programEditData = programEditLoadSettingUseCase(
                    inputData = ProgramEditLoadSettingUseCase.InputData(
                        programEdit = defaultProgramEdit
                    )
                )
                val programInitializeData = ProgramStateData.InitializeData(
                    programEdit = programEditData
                )
                present(data = programInitializeData)
            }

            is ProgramStateData.SaveData -> {
                //Save ProgramIndex
                programIndexSaveSettingUseCase(
                    inputData = ProgramIndexSaveSettingUseCase.InputData(
                        programIndex = programIndex
                    )
                )

                //Save ProgramDetails
                programDetailsSaveSettingUseCase(
                    inputData = ProgramDetailsSaveSettingUseCase.InputData(
                        programId = Optional.ofNullable(programDetails.programModel.getOrNull()?.id)
                    )
                )

                //Save ProgramEdit
                programEditSaveSettingUseCase(
                    inputData = ProgramEditSaveSettingUseCase.InputData(
                        programEdit = programEdit
                    )
                )
            }
        }
    }

    override fun toString(): String {
        return "ProgramStateModel(programDetails=%s, programIndex=%s, programSaveResult=%s)".format(
            programDetails,
            programIndex,
            programSaveResult
        )
    }

    companion object {

        fun new(
            crudProgramDataProvider: CRUDProgramDataProvider,
            findProgramDataProvider: FindProgramDataProvider,
            findProblemDataProvider: FindProblemDataProvider,
            rwSettingDataProvider: RWSettingDataProvider,
        ): ProgramStateModel = ProgramStateModel(
            deleteProgramUseCase = DeleteProgramInteractor(provider = crudProgramDataProvider),
            getDetailsProgramUseCase = GetDetailsProgramInteractor(provider = crudProgramDataProvider),
            getListProgramUseCase = GetListProgramInteractor(provider = findProgramDataProvider),
            saveProgramUseCase = SaveProgramInteractor(provider = crudProgramDataProvider),
            getListProblemUseCase = GetListProblemInteractor(provider = findProblemDataProvider),
            programIndexLoadSettingUseCase = ProgramIndexLoadSettingInteractor(
                useCase = LoadSettingInteractor(
                    provider = rwSettingDataProvider
                )
            ),
            programIndexSaveSettingUseCase = ProgramIndexSaveSettingInteractor(
                useCase = SaveSettingInteractor(
                    provider = rwSettingDataProvider
                )
            ),
            programDetailsLoadSettingUseCase = ProgramDetailsLoadSettingInteractor(
                useCase = LoadSettingInteractor(provider = rwSettingDataProvider)
            ),
            programDetailsSaveSettingUseCase = ProgramDetailsSaveSettingInteractor(
                useCase = SaveSettingInteractor(provider = rwSettingDataProvider)
            ),
            programEditLoadSettingUseCase = ProgramEditLoadSettingInteractor(
                useCase = LoadSettingInteractor(provider = rwSettingDataProvider)
            ),
            programEditSaveSettingUseCase = ProgramEditSaveSettingInteractor(
                useCase = SaveSettingInteractor(provider = rwSettingDataProvider)
            )
        )

    }
}
