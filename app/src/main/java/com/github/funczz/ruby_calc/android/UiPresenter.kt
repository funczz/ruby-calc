package com.github.funczz.ruby_calc.android

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import com.github.funczz.kotlin.logging.LoggerFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UiPresenter(

    uiState: UiState

) : Presenter<UiState> {

    private val logger = LoggerFactory.getLogger(this::class.java)

    private val _stateFlow: MutableStateFlow<UiState> = MutableStateFlow(
        uiState
    )

    override fun getStateFlow(): StateFlow<UiState> = _stateFlow.asStateFlow()

    @Composable
    override fun getState(): State<UiState> = getStateFlow().collectAsState()

    override fun render(output: UiState) {
        logger.info { "UiState=%s".format(output.toString()) }
        _stateFlow.value = output
    }

}
