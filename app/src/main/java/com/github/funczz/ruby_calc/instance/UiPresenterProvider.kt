package com.github.funczz.ruby_calc.instance

import com.github.funczz.kotlin.notifier.Notifier
import com.github.funczz.ruby_calc.android.MainApplication
import com.github.funczz.ruby_calc.android.UiPresenter
import com.github.funczz.ruby_calc.android.UiRepresentation
import com.github.funczz.ruby_calc.android.UiState
import com.github.funczz.ruby_calc.core.instance.InstanceHolder
import com.github.funczz.ruby_calc.core.instance.InstanceProvider

object UiPresenterProvider : InstanceProvider<UiPresenter> {

    override val name: String = this::class.qualifiedName!!

    override fun getInstanceHolder(): InstanceHolder {
        return MainApplication.getInstance()
    }

    fun new(notifier: Notifier): UiPresenter {
        val uiPresenter = UiPresenter(
            uiState = UiState()
        )
        UiRepresentation.subscribe(
            presenter = uiPresenter,
            notifier = notifier,
            executor = null,
        )
        return uiPresenter
    }
}
