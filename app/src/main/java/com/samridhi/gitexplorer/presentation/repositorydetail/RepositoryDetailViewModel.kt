package com.samridhi.gitexplorer.presentation.repositorydetail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samridhi.gitexplorer.data.models.reponse.GitHubRepo
import com.samridhi.gitexplorer.data.models.reponse.toContributorList
import com.samridhi.gitexplorer.domain.AppUseCase
import com.samridhi.gitexplorer.navigation.AppArgs
import com.samridhi.gitexplorer.presentation.home.HomeScreenUiState
import com.samridhi.gitexplorer.presentation.home.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepositoryDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val appUseCase: AppUseCase
) : ViewModel() {

    private val owner: String = savedStateHandle[AppArgs.ARG_OWNER]!!
    private val repo: String = savedStateHandle[AppArgs.ARG_REPO]!!

    var uiState by mutableStateOf(RepositoryDetailScreenUiState())
        private set

    init {
        fetchRepoDetail()
    }
    private suspend fun fetchContributorsDetail() {
        val response = appUseCase.getContributorsData(owner, repo)
        if (response.isSuccessful) {
            response.body()?.let {
                uiState = uiState.copy(
                    contributors = it.toContributorList()
                )
            }
        }

    }

    private fun fetchRepoDetail() {
        viewModelScope.launch(Dispatchers.IO) {
            val response1 = appUseCase.invoke(owner, repo)

            if (response1.isSuccessful) {
                response1.body()?.let {
                    uiState = uiState.copy(
                        screenState = ScreenState.DEFAULT,
                        repositoryDescription = it.description,
                        repositoryName = it.name,
                        projectLink = it.html_url,
                        userImage = it.owner.avatar_url,
                        stars = it.stargazers_count.toString(),
                        forks = it.forks_count.toString(),
                        issues = it.open_issues_count.toString(),
                        watchers = it.watchers_count.toString(),
                        lastUpdated = it.updated_at
                    )
                    fetchContributorsDetail()
                } ?: run {
                    uiState = uiState.copy(
                        screenState = ScreenState.EMPTY,
                        errorMessage = "No details Found"
                    )
                }
            } else {
                uiState = uiState.copy(
                    screenState = ScreenState.ERROR,
                    errorMessage = response1.errorBody().toString()
                )
            }
        }
    }
}

data class RepositoryDetailScreenUiState(
    val screenState: ScreenState = ScreenState.LOADING,
    val errorMessage: String = "",
    val repositoryDescription: String? = null,
    val repositoryName: String? = null,
    val projectLink: String? = null,
    val userImage: String? = null,
    val stars: String = "",
    val forks: String = "",
    val issues: String = "",
    val watchers: String = "",
    val lastUpdated: String = "",
    val contributors: List<Contributor> = emptyList(),
)

data class Contributor(
    val name: String = ""
)
