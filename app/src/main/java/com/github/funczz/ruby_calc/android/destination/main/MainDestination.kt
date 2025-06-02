package com.github.funczz.ruby_calc.android.destination.main

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import com.github.funczz.kotlin.notifier.Notifier
import com.github.funczz.ruby_calc.android.ObserveLifecycleEvent
import com.github.funczz.ruby_calc.android.Presenter
import com.github.funczz.ruby_calc.android.R
import com.github.funczz.ruby_calc.android.UiState
import com.github.funczz.ruby_calc.android.destination.problem.ProblemDestination
import com.github.funczz.ruby_calc.android.destination.problem.ProblemUiCommand
import com.github.funczz.ruby_calc.android.destination.program.ProgramDestination
import com.github.funczz.ruby_calc.android.destination.program.ProgramUiCommand
import com.github.funczz.ruby_calc.android.destination.system.SystemDestination
import com.github.funczz.ruby_calc.core.action.accept
import com.github.funczz.ruby_calc.core.model.RubyCalcStateData
import com.github.funczz.ruby_calc.core.model.program.ProgramEdit
import com.github.funczz.ruby_calc.core.model.program.ProgramStateData
import com.github.funczz.ruby_calc.core.service.BackupService
import java.util.concurrent.Executor

@Composable
fun MainDestination(
    presenter: Presenter<UiState>,
    notifier: Notifier,
    backupService: BackupService,
    executor: Executor,
) {
    ObserveLifecycleEvent { event ->
        when (event) {
            Lifecycle.Event.ON_CREATE -> {
                Log.d("LifecycleEvent", "ON_CREATE")
                notifier.accept(input = RubyCalcStateData.OnLoad)
                ProblemUiCommand.loadIndexSearchBoxUiState(presenter = presenter)
                ProgramUiCommand.loadIndexSearchBoxUiState(presenter = presenter)
                ProgramUiCommand.loadEditUiState(presenter = presenter)
            }

            Lifecycle.Event.ON_STOP -> {
                Log.d("LifecycleEvent", "ON_STOP")
                val programEditUiState = presenter.getStateFlow().value.programEditUiState
                notifier.accept(
                    input = RubyCalcStateData.InitializeData(
                        programInitializeData = ProgramStateData.InitializeData(
                            programEdit = ProgramEdit(
                                id = programEditUiState.id,
                                name = programEditUiState.name.value.text,
                                description = programEditUiState.description.value.text,
                                hint = programEditUiState.hint.value.text,
                                code = programEditUiState.code.value.text,
                            )
                        )
                    )
                )
                notifier.accept(input = RubyCalcStateData.OnSave)
            }

            Lifecycle.Event.ON_DESTROY -> {
                Log.d("LifecycleEvent", "ON_DESTROY")
                notifier.accept(input = RubyCalcStateData.OnClose)
            }

            else -> {}
        }
    }
    var currentDestination: MainDestinations by rememberSaveable { mutableStateOf(MainDestinations.PROBLEM) }
    NavigationSuiteScaffold(
        navigationSuiteItems = {
            MainDestinations.entries.forEach {
                val labelId = when (it) {
                    MainDestinations.PROBLEM -> R.string.problem_destination_label
                    MainDestinations.PROGRAM -> R.string.program_destination_label
                    MainDestinations.SETTINGS -> R.string.system_destination_label
                }
                this.item(
                    icon = {
                        Icon(it.icon, contentDescription = null)
                    },
                    label = { Text(text = stringResource(id = labelId)) },
                    selected = it == currentDestination,
                    onClick = { currentDestination = it }
                )
            }
        }
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val modifier = Modifier.fillMaxSize()
            when (currentDestination) {
                MainDestinations.PROBLEM -> ProblemDestination(
                    presenter = presenter,
                    notifier = notifier,
                    modifier = modifier,
                )

                MainDestinations.PROGRAM -> ProgramDestination(
                    presenter = presenter,
                    notifier = notifier,
                    modifier = modifier,
                )

                MainDestinations.SETTINGS -> SystemDestination(
                    presenter = presenter,
                    notifier = notifier,
                    backupService = backupService,
                    executor = executor,
                    modifier = modifier,
                )
            }
        }
    }
}
