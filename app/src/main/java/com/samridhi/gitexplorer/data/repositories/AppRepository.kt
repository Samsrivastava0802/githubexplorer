package com.samridhi.gitexplorer.data.repositories

import com.samridhi.gitexplorer.data.AppApiClientService
import javax.inject.Inject

class AppRepository @Inject constructor(
    private val service: AppApiClientService
) {
    suspend fun getSearchData(
        query: String,
        page : Int
    ) = service.searchRepository(
        query = query,
        page = page
    )

    suspend fun getRepoData(
        owner: String, repo: String
    ) = service.getRepositoryDetails(owner, repo)

    suspend fun getContributorsData(
        owner: String, repo: String
    ) = service.getContributors(owner, repo)
}