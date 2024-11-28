package com.samridhi.gitexplorer.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.samridhi.gitexplorer.presentation.home.HomeScreen
import com.samridhi.gitexplorer.presentation.repositorydetail.RepositoryDetailScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    startDestination: String = AppScreen.HomeScreen.route,
    navActions: AppNavigationActions
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(
            route = AppScreen.HomeScreen.route
        ) {
            HomeScreen(onAction = navActions::navigateFromHomeScreen)
        }
        composable(
            route = AppScreen.RepositoryDetailScreen.route
        ) {
           RepositoryDetailScreen()
        }
    }
}
