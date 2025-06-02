package com.github.funczz.ruby_calc.core.action

import com.github.funczz.kotlin.logging.LoggerFactory
import com.github.funczz.kotlin.notifier.Notifier
import com.github.funczz.ruby_calc.core.model.RubyCalcStateData
import com.github.funczz.ruby_calc.core.model.RubyCalcStateModel
import java.util.concurrent.Flow

class RubyCalcActionSubscriber(

    private val stateModel: RubyCalcStateModel,

    private val notifier: Notifier,

    ) : Flow.Subscriber<Any> {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun onSubscribe(subscription: Flow.Subscription?) {
    }

    override fun onError(throwable: Throwable?) {
    }

    override fun onComplete() {
    }

    override fun onNext(item: Any?) {
        logger.info { "item=%s".format(item?.toString()) }
        if (item !is RubyCalcStateData) return
        logger.info { "call `RubyCalcAction#accept`." }
        RubyCalcAction.accept(input = item, present = stateModel::present)
        logger.info { "call `notifier.post`." }
        notifier.post(item = stateModel, name = RubyCalcAction.REPRESENTATION)
    }

}


@Suppress("Unused")
fun Notifier.accept(input: RubyCalcStateData) {
    this.post(item = input, name = RubyCalcAction.ACCEPT)
}
