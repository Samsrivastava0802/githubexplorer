package com.samridhi.gitexplorer.navigation

import androidx.navigation.NavController
import com.samridhi.gitexplorer.util.withArg

class AppNavigationActions(
    private val navController: NavController,
    private val onFinish: () -> Unit
) {
    fun back() {
        navController.popBackStack()
    }
    private fun finishActivity() {
        onFinish()
    }
    fun navigateFromHomeScreen(actions: HomeScreenActions) {
        when (actions) {
            is HomeScreenActions.OpenRepositoryDetailScreen -> {
                navController.navigate(
                    AppScreen.RepositoryDetailScreen.name
                        .withArg(AppArgs.ARG_OWNER, actions.owner)
                        .withArg(AppArgs.ARG_REPO, actions.repo)
                )
            }
        }
    }
    fun navigateFromRepoDetailScreen(actions: RepositoryDetailsScreenActions) {
        when (actions) {
            RepositoryDetailsScreenActions.OnBack-> {
                back()
            }
        }
    }

}