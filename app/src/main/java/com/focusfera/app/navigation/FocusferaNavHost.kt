package com.focusfera.app.navigation
import com.focusfera.app.ui.focus.FocusSetupScreen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.focusfera.app.data.local.database.FocusferaDatabase
import com.focusfera.app.data.repository.SubjectRepository
import com.focusfera.app.ui.home.HomeScreen
import com.focusfera.app.ui.settings.SettingsScreen
import com.focusfera.app.ui.statistics.StatisticsScreen
import com.focusfera.app.ui.subjects.SubjectsScreen
import com.focusfera.app.ui.subjects.viewmodel.SubjectsViewModel
import com.focusfera.app.ui.subjects.viewmodel.SubjectsViewModelFactory
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.focusfera.app.ui.focus.FocusMode
import com.focusfera.app.ui.focus.FocusTimerScreen
import com.focusfera.app.ui.focus.viewmodel.FocusTimerViewModel

@Composable
fun FocusferaNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {

        composable(Screen.Home.route) {
            HomeScreen(
                onStartFocus = {
                    navController.navigate(
                        Screen.FocusSetup.route
                    )
                }
            )
        }

        composable(Screen.Subjects.route) {

            val context = LocalContext.current

            val database =
                FocusferaDatabase.getDatabase(context)

            val repository =
                SubjectRepository(
                    database.subjectDao()
                )

            val viewModel: SubjectsViewModel =
                viewModel(
                    factory = SubjectsViewModelFactory(
                        repository = repository
                    )
                )

            SubjectsScreen(
                viewModel = viewModel
            )
        }

        composable(Screen.Statistics.route) {
            StatisticsScreen()
        }

        composable(Screen.Settings.route) {
            SettingsScreen()
        }

        composable(
            route = Screen.FocusTimer.route,
            arguments = listOf(
                navArgument("subjectId") {
                    type = NavType.LongType
                },
                navArgument("focusMode") {
                    type = NavType.StringType
                },
                navArgument("durationMinutes") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->

            val subjectId =
                backStackEntry.arguments
                    ?.getLong("subjectId")
                    ?: return@composable

            val focusModeName =
                backStackEntry.arguments
                    ?.getString("focusMode")
                    ?: return@composable

            val durationMinutes =
                backStackEntry.arguments
                    ?.getInt("durationMinutes")
                    ?: return@composable

            val focusMode =
                FocusMode.valueOf(
                    focusModeName
                )

            val context =
                LocalContext.current

            val database =
                FocusferaDatabase
                    .getDatabase(context)

            val repository =
                SubjectRepository(
                    database.subjectDao()
                )

            val subjectsViewModel:
                    SubjectsViewModel =
                viewModel(
                    factory =
                        SubjectsViewModelFactory(
                            repository
                        )
                )

            val subjects by
            subjectsViewModel
                .subjects
                .collectAsStateWithLifecycle()

            val subjectName =
                subjects
                    .firstOrNull {
                        it.id == subjectId
                    }
                    ?.name
                    ?: "Matéria"

            val timerViewModel:
                    FocusTimerViewModel =
                viewModel()

            FocusTimerScreen(
                subjectName = subjectName,
                focusMode = focusMode,
                durationMinutes = durationMinutes,
                viewModel = timerViewModel,
                onExit = {
                    navController.popBackStack(
                        Screen.Home.route,
                        inclusive = false
                    )
                }
            )
        }

        composable(Screen.FocusSetup.route) {

            val context = LocalContext.current

            val database =
                FocusferaDatabase.getDatabase(context)

            val repository =
                SubjectRepository(
                    database.subjectDao()
                )

            val viewModel: SubjectsViewModel =
                viewModel(
                    factory = SubjectsViewModelFactory(
                        repository = repository
                    )
                )

            val subjects by
            viewModel.subjects.collectAsStateWithLifecycle()

            FocusSetupScreen(
                subjects = subjects,
                onContinue = {
                        subjectId,
                        focusMode,
                        durationMinutes ->

                    navController.navigate(
                        Screen.FocusTimer.createRoute(
                            subjectId = subjectId,
                            focusMode = focusMode.name,
                            durationMinutes = durationMinutes
                        )
                    )
                }
            )
        }
    }
}