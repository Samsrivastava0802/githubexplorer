package com.samridhi.gitexplorer.navigation

sealed class HomeScreenActions{
    data class OpenRepositoryDetailScreen(
        val owner: String,
        val repo: String,
    ) : HomeScreenActions()
}
sealed class RepositoryDetailsScreenActions{
    object OnBack : RepositoryDetailsScreenActions()
}