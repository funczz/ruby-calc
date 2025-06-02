package com.github.funczz.ruby_calc.android.destination.main

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
import com.github.funczz.kotlin.notifier.Notifier
import com.github.funczz.ruby_calc.android.Presenter
import com.github.funczz.ruby_calc.android.R
import com.github.funczz.ruby_calc.android.UiState
import com.github.funczz.ruby_calc.android.destination.problem.ProblemDestination
import com.github.funczz.ruby_calc.android.destination.program.ProgramDestination
import com.github.funczz.ruby_calc.android.destination.system.SystemDestination
import com.github.funczz.ruby_calc.core.service.BackupService
import java.util.concurrent.Executor

@Composable
fun MainDestination(
    presenter: Presenter<UiState>,
    notifier: Notifier,
    backupService: BackupService,
    executor: Executor,
) {
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
