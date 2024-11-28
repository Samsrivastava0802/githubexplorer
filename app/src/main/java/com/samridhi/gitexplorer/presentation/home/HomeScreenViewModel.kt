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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val appUseCase: AppUseCase
) : ViewModel() {
    var uiState by mutableStateOf(HomeScreenUiState())
        private set

    init {

    }

    fun onEvent(event: HomeScreenUIEvent) {
        when (event) {
            is HomeScreenUIEvent.SearchTextChanged -> {
                onSearchTextChange(event.text)
            }
            HomeScreenUIEvent.OnSearch -> {
                onSearch()
            }
        }
    }
    private fun onSearch(){
      if (uiState.searchFieldValue.text.isBlank()||uiState.searchFieldValue.text.isEmpty())
          return
        fetchRepository()
    }
    private fun fetchRepository(){
        viewModelScope.launch {
           val data = appUseCase.invoke(uiState.searchFieldValue.text)
            if (data.isSuccessful){
                uiState = uiState.copy(
                    items = data.body()?.items ?: emptyList()
                )
            }
        }
    }

    private fun onSearchTextChange(text: String) {
        uiState = uiState.copy(
            searchFieldValue = uiState.searchFieldValue.copy(text = text)
        )
    }
}

data class HomeScreenUiState(
    val screenState: ScreenState = ScreenState.LOADING,
    val searchFieldValue: TextFieldValue = TextFieldValue(),
    val items: List<GitHubRepo> = emptyList()
)

sealed class HomeScreenUIEvent {
    data class SearchTextChanged(val text: String) : HomeScreenUIEvent()
    object OnSearch: HomeScreenUIEvent()
}

enum class ScreenState {
    LOADING,
    EMPTY,
    ERROR,
    DEFAULT
}
