package com.github.funczz.ruby_calc.android.destination.system

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.funczz.kotlin.notifier.Notifier
import com.github.funczz.ruby_calc.android.Presenter
import com.github.funczz.ruby_calc.android.UiState
import com.github.funczz.ruby_calc.core.service.BackupService
import java.util.concurrent.Executor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SystemDestination(
    presenter: Presenter<UiState>,
    notifier: Notifier,
    backupService: BackupService,
    executor: Executor,
    modifier: Modifier,
) {
    //val uiState by presenter.getState()
    //val context = LocalContext.current
    //val owner = LocalLifecycleOwner.current

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = SystemDestinations.INDEX.name) {
        composable(SystemDestinations.INDEX.name) {
            SystemIndexScreen(
                presenter = presenter,
                notifier = notifier,
                backupService = backupService,
                executor = executor,
                navHostController = navController,
                modifier = modifier,
            )
        }
        composable(SystemDestinations.SHOW.name) {
            SystemShowScreen(
                presenter = presenter,
                navHostController = navController,
                modifier = modifier,
            )
        }
    }
}
