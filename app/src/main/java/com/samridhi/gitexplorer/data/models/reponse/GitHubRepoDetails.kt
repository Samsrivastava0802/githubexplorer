package com.samridhi.gitexplorer.data.models.reponse


data class GitHubRepoDetails(
    val name: String,
    val full_name: String,
    val description: String?,
    val owner: GitHubOwner,
    val language: String?,
    val stargazers_count: Int,
    val forks_count: Int,
    val open_issues_count: Int,
    val watchers_count: Int,
    val html_url: String,
    val created_at: String,
    val updated_at: String
)

data class GitHubOwner(
    val login: String,
    val avatar_url: String
)