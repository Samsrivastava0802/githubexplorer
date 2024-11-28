package com.samridhi.gitexplorer.domain

import com.samridhi.gitexplorer.data.repositories.AppRepository
import javax.inject.Inject

class AppUseCase @Inject constructor(
    private val appRepository: AppRepository
) {

    suspend operator fun invoke(query: String, page: Int) =
        appRepository.getSearchData(query, page = page)


    suspend operator fun invoke(
        owner: String,
        repo: String
    ) =
        appRepository.getRepoData(owner, repo)

    suspend fun getContributorsData(
        owner: String,
        repo: String
    ) = appRepository.getContributorsData(owner, repo)
}