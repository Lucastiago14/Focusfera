package com.focusfera.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.focusfera.app.ui.home.HomeScreen
import com.focusfera.app.ui.settings.SettingsScreen
import com.focusfera.app.ui.statistics.StatisticsScreen
import com.focusfera.app.ui.subjects.SubjectsScreen

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
            HomeScreen()
        }

        composable(Screen.Subjects.route) {
            SubjectsScreen()
        }

        composable(Screen.Statistics.route) {
            StatisticsScreen()
        }

        composable(Screen.Settings.route) {
            SettingsScreen()
        }
    }
}