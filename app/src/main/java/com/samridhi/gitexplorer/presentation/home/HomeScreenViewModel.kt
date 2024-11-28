package com.samridhi.gitexplorer.presentation.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samridhi.gitexplorer.data.models.reponse.GitHubRepo
import com.samridhi.gitexplorer.domain.AppUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val appUseCase: AppUseCase
) : ViewModel() {
    var uiState by mutableStateOf(HomeScreenUiState())
        private set

    private var currentPage = 1
    private var defaultSearchText = "clone"
    private var isSearching = false

    init {
        fetchRepository(defaultSearchText)
    }

    fun onEvent(event: HomeScreenUIEvent) {
        when (event) {
            is HomeScreenUIEvent.SearchTextChanged -> {
                onSearchTextChange(event.text)
            }

            HomeScreenUIEvent.OnSearch -> {
                onSearch()
            }

            HomeScreenUIEvent.LoadMore -> {
                loadMore()
            }
        }
    }

    private fun onSearch() {
        if (uiState.searchFieldValue.text.isBlank() || uiState.searchFieldValue.text.isEmpty()) return
        uiState = uiState.copy(screenState = ScreenState.LOADING)
        isSearching = true
        fetchRepository(uiState.searchFieldValue.text)
    }

    private fun fetchRepository(text: String) {
        viewModelScope.launch {
            val data = appUseCase.invoke(text, currentPage)
            uiState = if (data.isSuccessful) {
                val newItems = data.body()?.items ?: emptyList()
                if (isSearching) {
                    uiState = uiState.copy(
                        items = emptyList()
                    )
                    isSearching = false
                }
                uiState.copy(
                    items = if (currentPage == 1) newItems else uiState.items + newItems,
                    screenState = if (data.body()?.items?.isNotEmpty() == true) {
                        ScreenState.DEFAULT
                    } else {
                        ScreenState.EMPTY
                    },
                    isLoadingMore = false
                )
            } else {
                isSearching = false
                uiState.copy(screenState = ScreenState.ERROR, isLoadingMore = false)
            }
        }
    }

    private fun onSearchTextChange(text: String) {
        uiState = uiState.copy(
            searchFieldValue = uiState.searchFieldValue.copy(text = text),
            isLoadingMore = false
        )
    }

    private fun loadMore() {
        viewModelScope.launch {
            if (uiState.isLoadingMore) return@launch
            uiState = uiState.copy(isLoadingMore = true)
            delay(2000)
            currentPage++
            fetchRepository(
                if (uiState.searchFieldValue.text.isNotEmpty()) {
                    uiState.searchFieldValue.text
                } else {
                    defaultSearchText
                }
            )
        }

    }
}

data class HomeScreenUiState(
    val screenState: ScreenState = ScreenState.LOADING,
    val searchFieldValue: TextFieldValue = TextFieldValue(),
    val items: List<GitHubRepo> = emptyList(),
    val isLoadingMore: Boolean = false
) {
    fun getOwnerName(item: GitHubRepo) = item.full_name.split("/")[0]
    fun getRepoName(item: GitHubRepo) = item.full_name.split("/")[1]
}

sealed class HomeScreenUIEvent {
    data class SearchTextChanged(val text: String) : HomeScreenUIEvent()
    object OnSearch : HomeScreenUIEvent()
    object LoadMore : HomeScreenUIEvent()
}

enum class ScreenState {
    LOADING,
    EMPTY,
    ERROR,
    DEFAULT
}
