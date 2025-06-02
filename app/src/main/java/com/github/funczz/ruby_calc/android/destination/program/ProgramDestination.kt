package com.github.funczz.ruby_calc.android.destination.program

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.funczz.kotlin.notifier.Notifier
import com.github.funczz.ruby_calc.android.Presenter
import com.github.funczz.ruby_calc.android.UiState

@Composable
fun ProgramDestination(
    presenter: Presenter<UiState>,
    notifier: Notifier,
    modifier: Modifier,
) {
    //val uiState by presenter.getState()
    //val context = LocalContext.current
    //val owner = LocalLifecycleOwner.current

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = ProgramDestinations.INDEX.name) {
        composable(ProgramDestinations.INDEX.name) {
            ProgramIndexScreen(
                presenter = presenter,
                notifier = notifier,
                navHostController = navController,
                modifier = modifier,
            )
        }
        composable(ProgramDestinations.CREATE.name) {
            ProgramEditScreen(
                presenter = presenter,
                notifier = notifier,
                navHostController = navController,
                modifier = modifier,
            )
        }
        composable(ProgramDestinations.SHOW.name) {
            ProgramShowScreen(
                presenter = presenter,
                notifier = notifier,
                navHostController = navController,
                modifier = modifier,
            )
        }
        composable(ProgramDestinations.EDIT.name) {
            ProgramEditScreen(
                presenter = presenter,
                notifier = notifier,
                navHostController = navController,
                modifier = modifier,
            )
        }
    }
}
