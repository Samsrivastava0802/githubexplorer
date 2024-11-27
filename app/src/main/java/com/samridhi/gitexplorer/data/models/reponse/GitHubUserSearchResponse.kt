package com.samridhi.gitexplorer.data.models.reponse
data class GitHubUserSearchResponse(
    val total_Count: Int,
    val items: List<GitHubRepo>
)
data class GitHubRepo(
    val name: String,
    val full_name: String,
    val html_url: String,
    val description: String?,
    val language: String?,
    val stargazers_count: Int
)
