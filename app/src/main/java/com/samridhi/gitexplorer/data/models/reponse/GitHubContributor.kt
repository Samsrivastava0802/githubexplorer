package com.samridhi.gitexplorer.data.models.reponse

data class GitHubContributor(
    val login: String,
    val avatar_url: String,
    val html_url: String,
    val contributions: Int
)