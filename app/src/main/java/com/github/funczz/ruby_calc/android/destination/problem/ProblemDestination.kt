package com.github.funczz.ruby_calc.android.destination.problem

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.github.funczz.kotlin.notifier.Notifier
import com.github.funczz.ruby_calc.android.Presenter
import com.github.funczz.ruby_calc.android.UiState

@Composable
fun ProblemDestination(
    presenter: Presenter<UiState>,
    notifier: Notifier,
    modifier: Modifier,
) {
    //val uiState by presenter.getState()
    //val context = LocalContext.current
    //val owner = LocalLifecycleOwner.current

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = ProblemDestinations.INDEX.name) {
        composable(ProblemDestinations.INDEX.name) {
            ProblemIndexScreen(
                presenter = presenter,
                notifier = notifier,
                navHostController = navController,
                modifier = modifier,
            )
        }
        composable(ProblemDestinations.CREATE.name) {
            ProblemEditScreen(
                presenter = presenter,
                notifier = notifier,
                navHostController = navController,
                modifier = modifier,
                isNewProblem = true,
            )
        }
        composable(ProblemDestinations.SHOW.name) {
            ProblemShowScreen(
                presenter = presenter,
                notifier = notifier,
                navHostController = navController,
                modifier = modifier,
            )
        }
        composable(ProblemDestinations.EDIT.name) {
            ProblemEditScreen(
                presenter = presenter,
                notifier = notifier,
                navHostController = navController,
                modifier = modifier,
            )
        }
        composable(
            route = "%s/{id}".format(ProblemDestinations.RESULT.name),
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { backStackEntry ->
            ProblemResultScreen(
                id = backStackEntry.arguments?.getLong("id"),
                presenter = presenter,
                notifier = notifier,
                navHostController = navController,
                modifier = modifier,
            )
        }
    }
}
