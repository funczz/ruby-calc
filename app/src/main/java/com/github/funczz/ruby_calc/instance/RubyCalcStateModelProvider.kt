package com.github.funczz.ruby_calc.instance

import com.github.funczz.kotlin.notifier.Notifier
import com.github.funczz.ruby_calc.android.MainApplication
import com.github.funczz.ruby_calc.core.action.RubyCalcAction
import com.github.funczz.ruby_calc.core.instance.InstanceHolder
import com.github.funczz.ruby_calc.core.instance.InstanceProvider
import com.github.funczz.ruby_calc.core.model.RubyCalcStateModel
import com.github.funczz.ruby_calc.core.model.answer.AnswerIndex
import com.github.funczz.ruby_calc.core.model.answer.AnswerStateData
import com.github.funczz.ruby_calc.core.service.RubyService
import com.github.funczz.ruby_calc.core.vo.OrderBy
import com.github.funczz.ruby_calc.data.db.RubyCalcDBResources
import com.github.funczz.ruby_calc.data.model.RubyCalStateModelFactory
import java.util.concurrent.Executor

object RubyCalcStateModelProvider : InstanceProvider<RubyCalcStateModel> {

    override val name: String = this::class.qualifiedName!!

    override fun getInstanceHolder(): InstanceHolder {
        return MainApplication.getInstance()
    }

    fun new(
        dbResources: RubyCalcDBResources,
        rubyService: RubyService,
        notifier: Notifier,
        executor: Executor?,
    ): RubyCalcStateModel {
        val stateModel = RubyCalStateModelFactory.new(
            dbResources = dbResources, rubyService = rubyService
        )
        stateModel.present(
            data = AnswerStateData.InitializeData(
                answerIndex = AnswerIndex(orderBy = OrderBy.DESC)
            )
        )
        RubyCalcAction.subscribe(
            stateModel = stateModel,
            notifier = notifier,
            executor = executor
        )
        return stateModel
    }
}
