package com.github.funczz.ruby_calc.android

import com.github.funczz.kotlin.logging.LoggerFactory
import com.github.funczz.ruby_calc.core.model.RubyCalcStateModel
import java.util.concurrent.Flow

class UiRepresentationSubscriber(

    private val presenter: Presenter<UiState>

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
        when {
            item is RubyCalcStateModel -> {
                logger.info { "call `UiRepresentation#representation`." }
                UiRepresentation.representation(
                    model = Pair(item, presenter.getStateFlow().value),
                    render = presenter::render
                )
            }
        }
    }
}
