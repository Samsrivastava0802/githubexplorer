package com.samridhi.gitexplorer.data

import com.samridhi.gitexplorer.data.models.reponse.GitHubContributor
import com.samridhi.gitexplorer.data.models.reponse.GitHubRepoDetails
import com.samridhi.gitexplorer.data.models.reponse.GitHubUserSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AppApiClientService {

    @GET("search/repositories")
    suspend fun searchRepository(
        @Query("q") query: String
    ): Response<GitHubUserSearchResponse>

    // to get detail of a repository
    @GET("repos/{owner}/{repo}")
    suspend fun getRepositoryDetails(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): Response<GitHubRepoDetails>

    // to get detail of a repository contributors
    @GET("repos/{owner}/{repo}/contributors")
    suspend fun getContributors(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): Response<List<GitHubContributor>>
}