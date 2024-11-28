package com.samridhi.gitexplorer.navigation

import com.samridhi.gitexplorer.navigation.AppArgs.ARG_OWNER
import com.samridhi.gitexplorer.navigation.AppArgs.ARG_REPO
import com.samridhi.gitexplorer.util.addRouteArgument

object AppArgs {
    const val ARG_OWNER = "owner"
    const val ARG_REPO = "repo"
}

sealed class AppScreen(val name: String, val route: String) {
    data object HomeScreen : AppScreen("home", "home")
    data object RepositoryDetailScreen : AppScreen(
        "repositoryDetail", "repositoryDetail"
            .addRouteArgument(ARG_OWNER)
            .addRouteArgument(ARG_REPO)
    )
}
