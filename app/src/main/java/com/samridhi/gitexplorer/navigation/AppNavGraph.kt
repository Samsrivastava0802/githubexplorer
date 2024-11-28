package com.samridhi.gitexplorer.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.samridhi.gitexplorer.presentation.home.HomeScreen
import com.samridhi.gitexplorer.presentation.repositorydetail.RepositoryDetailScreen

@Composable
fun AppNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = "home",
    ) {
        composable(
            route = "home"
        ) {
            HomeScreen()
        }
        composable(
            route = "repoDetailScreen"
        ) {
           RepositoryDetailScreen()
        }
    }
}