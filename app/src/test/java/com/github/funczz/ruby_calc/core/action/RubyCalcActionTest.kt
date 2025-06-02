package com.github.funczz.ruby_calc.core.action

import com.github.funczz.kotlin.notifier.DefaultNotifierSubscription
import com.github.funczz.kotlin.notifier.Notifier
import com.github.funczz.ruby_calc.core.model.RubyCalcStateModel
import com.github.funczz.ruby_calc.core.model.program.ProgramStateData
import com.github.funczz.ruby_calc.core.usecase.program.SaveProgramUseCase
import com.github.funczz.ruby_calc.data.db.entity.program
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.ktorm.entity.count
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import util.DBTestUtil
import util.RubyCalcStateModelUtil
import java.util.Optional
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Flow
import java.util.concurrent.Flow.Subscriber

@RunWith(RobolectricTestRunner::class)
@Config(
    manifest = "src/main/AndroidManifest.xml",
    packageName = "com.github.funczz.ruby_calc.android"
)
class RubyCalcActionTest {

    @Test
    fun `save - program`() {
        assertEquals(0, dbTestUtil.database.program.count())
        val inputData = ProgramStateData.InputData(
            value = SaveProgramUseCase.InputData(
                id = null,
                name = "greeting",
                description = "greeting(EN)",
                hint = "-",
                code = "hello world.",
                createdAt = null,
                updatedAt = null,
            )
        )
        notifier.accept(input = inputData)
        //action
        DBTestUtil.assertWait({ stateModel.programStateModel.programIndex.programList.isNotEmpty() }) {
            assertEquals(
                1,
                stateModel.programStateModel.programIndex.programList.size
            )
        }
        //representation
        DBTestUtil.assertWait({ subscriber.model.isPresent }) {
            assertEquals(1, subscriber.model.get().programStateModel.programIndex.programList.size)
        }
    }

    private val dbTestUtil = DBTestUtil()

    private lateinit var stateModel: RubyCalcStateModel

    private lateinit var notifier: Notifier

    private lateinit var executor: Executor

    private lateinit var subscriber: RepresentationSubscriber

    @Before
    fun beforeEach() {
        dbTestUtil.setUp()
        stateModel = RubyCalcStateModelUtil.new(dbTestUtil = dbTestUtil)
        executor = Executors.newCachedThreadPool()
        notifier = Notifier()
        RubyCalcAction.subscribe(stateModel = stateModel, notifier = notifier, executor = null)
        subscriber = RepresentationSubscriber()
        subscriber.subscribe(notifier = notifier, executor = null)
    }

    @After
    fun afterEach() {
        dbTestUtil.tearDown()
        notifier.unsubscribeAll()
        (executor as ExecutorService).shutdownNow()
    }

    private class RepresentationSubscriber : Subscriber<Any> {

        var model: Optional<RubyCalcStateModel> = Optional.empty()

        fun subscribe(notifier: Notifier, executor: Executor?) {
            notifier.subscribe(
                subscription = DefaultNotifierSubscription(
                    subscriber = this,
                    name = RubyCalcAction.REPRESENTATION_REGISTERED_NAME,
                    executor = Optional.ofNullable(executor)
                ),
                executor = executor
            )
        }

        override fun onSubscribe(subscription: Flow.Subscription?) {
        }

        override fun onNext(item: Any?) {
            if (item is RubyCalcStateModel) model = Optional.ofNullable(item)
        }

        override fun onError(throwable: Throwable?) {
        }

        override fun onComplete() {
        }
    }

}
