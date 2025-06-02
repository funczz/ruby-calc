package com.github.funczz.ruby_calc.core.action

import com.github.funczz.kotlin.notifier.DefaultNotifierSubscription
import com.github.funczz.kotlin.notifier.Notifier
import com.github.funczz.kotlin.sam.SamAction
import com.github.funczz.ruby_calc.core.model.RubyCalcStateData
import com.github.funczz.ruby_calc.core.model.RubyCalcStateModel
import java.util.Optional
import java.util.concurrent.Executor

@Suppress("Unused", "MemberVisibilityCanBePrivate")
object RubyCalcAction : SamAction<RubyCalcStateData, RubyCalcStateData> {

    override fun accept(input: RubyCalcStateData, present: (RubyCalcStateData) -> Unit) {
        present(input)
    }

    fun subscribe(stateModel: RubyCalcStateModel, notifier: Notifier, executor: Executor?) {
        notifier.subscribe(
            subscription = DefaultNotifierSubscription(
                subscriber = RubyCalcActionSubscriber(stateModel = stateModel, notifier = notifier),
                name = ACCEPT_REGISTERED_NAME,
                executor = Optional.ofNullable(executor)
            ),
            executor = executor
        )
    }

    const val ACCEPT_REGISTERED_NAME = "accept"

    val ACCEPT = "^%s$".format(ACCEPT_REGISTERED_NAME).toRegex()

    const val REPRESENTATION_REGISTERED_NAME = "representation"

    val REPRESENTATION = "^%s$".format(REPRESENTATION_REGISTERED_NAME).toRegex()

}
