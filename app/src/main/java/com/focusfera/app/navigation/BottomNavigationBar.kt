package com.focusfera.app.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationBar(
    navController: NavHostController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {

        NavigationBarItem(
            selected = currentRoute == Screen.Home.route,
            onClick = {
                navController.navigate(Screen.Home.route) {
                    launchSingleTop = true
                    restoreState = true

                    popUpTo(Screen.Home.route) {
                        saveState = true
                    }
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Início"
                )
            },
            label = {
                Text("Início")
            }
        )

        NavigationBarItem(
            selected = currentRoute == Screen.Subjects.route,
            onClick = {
                navController.navigate(Screen.Subjects.route) {
                    launchSingleTop = true
                    restoreState = true

                    popUpTo(Screen.Home.route) {
                        saveState = true
                    }
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.MenuBook,
                    contentDescription = "Matérias"
                )
            },
            label = {
                Text("Matérias")
            }
        )

        NavigationBarItem(
            selected = currentRoute == Screen.Statistics.route,
            onClick = {
                navController.navigate(Screen.Statistics.route) {
                    launchSingleTop = true
                    restoreState = true

                    popUpTo(Screen.Home.route) {
                        saveState = true
                    }
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.BarChart,
                    contentDescription = "Estatísticas"
                )
            },
            label = {
                Text("Estatísticas")
            }
        )

        NavigationBarItem(
            selected = currentRoute == Screen.Settings.route,
            onClick = {
                navController.navigate(Screen.Settings.route) {
                    launchSingleTop = true
                    restoreState = true

                    popUpTo(Screen.Home.route) {
                        saveState = true
                    }
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Configurações"
                )
            },
            label = {
                Text("Ajustes")
            }
        )
    }
}