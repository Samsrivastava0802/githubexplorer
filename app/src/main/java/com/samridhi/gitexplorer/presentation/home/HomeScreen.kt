package com.samridhi.gitexplorer.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.samridhi.gitexplorer.R
import com.samridhi.gitexplorer.data.models.reponse.GitHubRepo
import com.samridhi.gitexplorer.navigation.HomeScreenActions
import com.samridhi.gitexplorer.presentation.common.CustomTextField
import com.samridhi.gitexplorer.presentation.common.ErrorMessage
import com.samridhi.gitexplorer.presentation.common.HorizontalProgressLoader
import com.samridhi.gitexplorer.presentation.common.ProgressLoader
import com.samridhi.gitexplorer.presentation.common.getBackgroundColor
import com.samridhi.gitexplorer.presentation.ui.Grey
import com.samridhi.gitexplorer.presentation.ui.Grey400
import com.samridhi.gitexplorer.presentation.ui.paragraph
import com.samridhi.gitexplorer.presentation.ui.paragraphDefaultRegular
import com.samridhi.gitexplorer.util.AppDrawable
import com.samridhi.gitexplorer.util.AppString
import com.samridhi.gitexplorer.util.isScrolledToTheEnd

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = hiltViewModel(),
    onAction: (homeScreenActions: HomeScreenActions) -> Unit
) {
    HomeScreenContent(
        uiState = viewModel.uiState,
        onEvent = viewModel::onEvent,
        onCardClick = { owner, repo ->
            onAction(HomeScreenActions.OpenRepositoryDetailScreen(owner, repo))
        }
    )
}

@Composable
fun HomeScreenContent(
    uiState: HomeScreenUiState,
    onEvent: (HomeScreenUIEvent) -> Unit,
    onCardClick: (String, String) -> Unit
) {
    when (uiState.screenState) {

        ScreenState.EMPTY -> {
            ErrorMessage(errorMessage = stringResource(id = R.string.no_data_available))
        }

        ScreenState.ERROR -> {
            ErrorMessage(
                errorMessage = stringResource(id = R.string.something_went_wrong),
                error = true
            )
        }

        else -> {
            val listState = rememberLazyListState()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Grey)
                    .padding(start = 16.dp, end = 16.dp, bottom = 24.dp)
            ) {
                Spacer(modifier = Modifier.size(24.dp))
                CustomTextField(
                    textFieldState = uiState.searchFieldValue,
                    allowSpecialCharacters = false,
                    placeHolder = {
                        Text(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            text = stringResource(id = AppString.search_repository),
                            style = MaterialTheme.typography.paragraph,
                            color = Grey400
                        )
                    },
                    trailingIcon = {
                        Icon(
                            modifier = Modifier.clickable {
                                onEvent(HomeScreenUIEvent.OnSearch)
                            },
                            painter = painterResource(id = AppDrawable.ic_search_16px),
                            tint = Grey400,
                            contentDescription = ""
                        )
                    },
                    onValueChange = {
                        onEvent(HomeScreenUIEvent.SearchTextChanged(it))
                    }
                )
                Spacer(modifier = Modifier.size(24.dp))
                if (uiState.screenState == ScreenState.LOADING) {
                    ProgressLoader()
                } else {
                    LazyColumn(state = listState) {
                        items(uiState.items.size) {
                            val data = uiState.items[it]
                            RepositoryItem(
                                data = data,
                                onClick = {
                                    onCardClick(
                                        uiState.getOwnerName(data),
                                        uiState.getRepoName(data),
                                    )
                                }
                            )
                            Spacer(modifier = Modifier.size(24.dp))
                        }
                        item {
                            if (uiState.isLoadingMore) {
                                HorizontalProgressLoader()
                            }
                        }
                    }
                    if (listState.isScrolledToTheEnd()) {
                        onEvent(HomeScreenUIEvent.LoadMore)
                    }
                }
            }
        }
    }

}

@Composable
fun RepositoryItem(
    data: GitHubRepo,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(8.dp),
                clip = false
            )
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .clickable {
                onClick()
            }
            .padding(16.dp)

    ) {
        Text(
            text = data.name,
            style = MaterialTheme.typography.paragraphDefaultRegular
        )
        Spacer(modifier = Modifier.size(24.dp))
        Text(
            text = data.description ?: "hi",
            maxLines = 4,
            style = MaterialTheme.typography.paragraph
        )
        Spacer(modifier = Modifier.size(24.dp))
        Row{
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .background(color = Color(getBackgroundColor(data.language ?: "a")), shape = CircleShape)
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = data.language ?: "kotlin",
                    style = MaterialTheme.typography.paragraph.copy(fontSize = 12.sp)
                )
            }
            Spacer(modifier = Modifier.size(24.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(14.dp),
                    painter = painterResource(id = AppDrawable.star),
                    contentDescription = ""
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = data.stargazers_count.toString(),
                    style = MaterialTheme.typography.paragraph.copy(fontSize = 12.sp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
//    HomeScreenContent(
//        uiState = HomeScreenUiState(),
//        onEvent = {},
//        onCardClick = { owner, repo -> }
//    )
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        RepositoryItem(
            data = GitHubRepo(
                name = "LearnIt",
                full_name = "meesho/LearnIt",
                html_url = "https://github.com/meesho/LearnIt",
                description = "An educational app to simplify learning",
                language = "Kotlin",
                stargazers_count = 123
            ),
            onClick = {

            }
        )
    }
}