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
                onStartTimer = {
                    // Será implementado na fase do timer.
                },
                onStartPomodoro = {
                    // Será implementado depois.
                },
                onStartStopwatch = {
                    // Será implementado depois.
                }
            )
        }
    }
}