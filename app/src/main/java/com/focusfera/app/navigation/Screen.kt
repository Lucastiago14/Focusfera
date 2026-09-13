package com.focusfera.app.navigation

sealed class Screen(
    val route: String
) {

    data object Home : Screen("home")

    data object Subjects : Screen("subjects")

    data object Statistics : Screen("statistics")

    data object Settings : Screen("settings")

    data object FocusSetup : Screen("focus_setup")

    data object FocusTimer :
        Screen(
            "focus_timer/{subjectId}/{focusMode}/{durationMinutes}"
        ) {

        fun createRoute(
            subjectId: Long,
            focusMode: String,
            durationMinutes: Int
        ): String {
            return "focus_timer/" +
                    "$subjectId/" +
                    "$focusMode/" +
                    "$durationMinutes"
        }
    }
}