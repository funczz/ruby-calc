package com.github.funczz.ruby_calc.core.model.program

import com.github.funczz.kotlin.logging.LoggerFactory
import com.github.funczz.kotlin.sam.SamModel
import com.github.funczz.ruby_calc.core.data.provider.problem.FindProblemDataProvider
import com.github.funczz.ruby_calc.core.data.provider.program.CRUDProgramDataProvider
import com.github.funczz.ruby_calc.core.data.provider.program.FindProgramDataProvider
import com.github.funczz.ruby_calc.core.interactor.problem.GetListProblemInteractor
import com.github.funczz.ruby_calc.core.interactor.program.DeleteProgramInteractor
import com.github.funczz.ruby_calc.core.interactor.program.GetDetailsProgramInteractor
import com.github.funczz.ruby_calc.core.interactor.program.GetListProgramInteractor
import com.github.funczz.ruby_calc.core.interactor.program.SaveProgramInteractor
import com.github.funczz.ruby_calc.core.usecase.problem.GetListProblemUseCase
import com.github.funczz.ruby_calc.core.usecase.program.DeleteProgramUseCase
import com.github.funczz.ruby_calc.core.usecase.program.GetDetailsProgramUseCase
import com.github.funczz.ruby_calc.core.usecase.program.GetListProgramUseCase
import com.github.funczz.ruby_calc.core.usecase.program.SaveProgramUseCase
import java.util.Optional

@Suppress("Unused", "MemberVisibilityCanBePrivate")
class ProgramStateModel(

    private val deleteProgramUseCase: DeleteProgramUseCase,

    private val getDetailsProgramUseCase: GetDetailsProgramUseCase,

    private val getListProgramUseCase: GetListProgramUseCase,

    private val saveProgramUseCase: SaveProgramUseCase,

    private val getListProblemUseCase: GetListProblemUseCase,

    programDetails: ProgramDetails = ProgramDetails(),

    programIndex: ProgramIndex = ProgramIndex(),

    programSaveResult: ProgramSaveResult = ProgramSaveResult(),

    programSelection: ProgramIndex = ProgramIndex(),

    ) : SamModel<ProgramStateData> {

    var programDetails: ProgramDetails = programDetails
        private set

    var programIndex: ProgramIndex = programIndex
        private set

    var programSaveResult: ProgramSaveResult = programSaveResult
        private set

    var programSelection: ProgramIndex = programSelection
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
        ): ProgramStateModel = ProgramStateModel(
            deleteProgramUseCase = DeleteProgramInteractor(provider = crudProgramDataProvider),
            getDetailsProgramUseCase = GetDetailsProgramInteractor(provider = crudProgramDataProvider),
            getListProgramUseCase = GetListProgramInteractor(provider = findProgramDataProvider),
            saveProgramUseCase = SaveProgramInteractor(provider = crudProgramDataProvider),
            getListProblemUseCase = GetListProblemInteractor(provider = findProblemDataProvider),
        )
    }
}
