package com.github.funczz.ruby_calc.core.model.answer

import com.github.funczz.kotlin.logging.LoggerFactory
import com.github.funczz.kotlin.sam.SamModel
import com.github.funczz.ruby_calc.core.data.provider.answer.CRUDAnswerDataProvider
import com.github.funczz.ruby_calc.core.data.provider.answer.FindAnswerDataProvider
import com.github.funczz.ruby_calc.core.interactor.answer.DeleteAnswerInteractor
import com.github.funczz.ruby_calc.core.interactor.answer.GetDetailsAnswerInteractor
import com.github.funczz.ruby_calc.core.interactor.answer.GetListAnswerInteractor
import com.github.funczz.ruby_calc.core.interactor.answer.SaveAnswerInteractor
import com.github.funczz.ruby_calc.core.usecase.answer.DeleteAnswerUseCase
import com.github.funczz.ruby_calc.core.usecase.answer.GetDetailsAnswerUseCase
import com.github.funczz.ruby_calc.core.usecase.answer.GetListAnswerUseCase
import com.github.funczz.ruby_calc.core.usecase.answer.SaveAnswerUseCase
import java.util.Optional

@Suppress("Unused", "MemberVisibilityCanBePrivate")
class AnswerStateModel(

    private val deleteAnswerUseCase: DeleteAnswerUseCase,

    private val getDetailsAnswerUseCase: GetDetailsAnswerUseCase,

    private val getListAnswerUseCase: GetListAnswerUseCase,

    private val saveAnswerUseCase: SaveAnswerUseCase,

    answerDetails: AnswerDetails = AnswerDetails(),

    answerIndex: AnswerIndex = AnswerIndex(),

    ) : SamModel<AnswerStateData> {

    var answerDetails: AnswerDetails = answerDetails
        private set

    var answerIndex: AnswerIndex = answerIndex
        private set

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun present(data: AnswerStateData) {
        when (data) {
            is AnswerStateData.InitializeData -> {
                logger.info { "AnswerStateData.InitializeData=%s".format(data.toString()) }
                data.answerDetails?.let { answerDetails = it }
                data.answerIndex?.let { answerIndex = it }
            }

            is AnswerStateData.InputData -> {
                when (val v = data.value) {
                    is DeleteAnswerUseCase.InputData -> {
                        logger.info { "DeleteAnswerUseCase.InputData=%s".format(v.toString()) }
                        deleteAnswerUseCase(inputData = v)
                    }

                    is GetDetailsAnswerUseCase.InputData -> {
                        logger.info { "GetDetailsAnswerUseCase.InputData=%s".format(v.toString()) }
                        answerDetails = AnswerDetails(
                            answerModel = getDetailsAnswerUseCase(inputData = v),
                        )
                    }

                    is GetListAnswerUseCase.InputData -> {
                        logger.info { "GetListAnswerUseCase.InputData=%s".format(v.toString()) }
                        answerIndex = AnswerIndex(
                            answerList = getListAnswerUseCase(inputData = v),
                            problemId = Optional.ofNullable(v.problemId),
                            value = v.value,
                            orderBy = v.orderBy,
                            limit = v.limit,
                        )
                    }

                    is SaveAnswerUseCase.InputData -> {
                        logger.info { "SaveAnswerUseCase.InputData=%s".format(v.toString()) }
                        saveAnswerUseCase(inputData = v)
                    }
                }
            }
        }
    }

    override fun toString(): String {
        return "AnswerStateModel(answerDetails=%s, answerIndex=%s)".format(
            answerDetails,
            answerIndex
        )
    }

    companion object {

        fun new(
            crudAnswerDataProvider: CRUDAnswerDataProvider,
            findAnswerDataProvider: FindAnswerDataProvider,
        ): AnswerStateModel = AnswerStateModel(
            deleteAnswerUseCase = DeleteAnswerInteractor(provider = crudAnswerDataProvider),
            getDetailsAnswerUseCase = GetDetailsAnswerInteractor(provider = crudAnswerDataProvider),
            getListAnswerUseCase = GetListAnswerInteractor(provider = findAnswerDataProvider),
            saveAnswerUseCase = SaveAnswerInteractor(provider = crudAnswerDataProvider),
        )
    }
}
