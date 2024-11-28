package com.samridhi.gitexplorer.data.models.reponse

import com.samridhi.gitexplorer.presentation.repositorydetail.Contributor

data class GitHubContributor(
    val login: String,
    val avatar_url: String,
    val html_url: String,
    val contributions: Int
)
fun List<GitHubContributor>.toContributorList(): List<Contributor> {
    return this.map { githubContributor ->
        Contributor(name = githubContributor.login)
    }
}
