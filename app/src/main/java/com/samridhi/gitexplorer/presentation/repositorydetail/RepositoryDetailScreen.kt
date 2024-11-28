package com.samridhi.gitexplorer.presentation.repositorydetail

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.samridhi.gitexplorer.R
import com.samridhi.gitexplorer.app.App
import com.samridhi.gitexplorer.navigation.HomeScreenActions
import com.samridhi.gitexplorer.navigation.RepositoryDetailsScreenActions
import com.samridhi.gitexplorer.presentation.common.ErrorMessage
import com.samridhi.gitexplorer.presentation.common.ProgressLoader
import com.samridhi.gitexplorer.presentation.home.ScreenState
import com.samridhi.gitexplorer.util.AppDrawable
import com.samridhi.gitexplorer.util.AppString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepositoryDetailScreen(
    viewModel: RepositoryDetailViewModel = hiltViewModel(),
    onAction: (repositoryDetailsScreenActions: RepositoryDetailsScreenActions) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = AppString.search_repository),
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 14.sp)
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onAction(RepositoryDetailsScreenActions.OnBack)
                        }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        RepositoryDetailScreenContent(
            modifier = Modifier.padding(innerPadding),
            uiState = viewModel.uiState
        )
    }
}

@Composable
fun RepositoryDetailScreenContent(
    modifier: Modifier,
    uiState: RepositoryDetailScreenUiState
) {
    when (uiState.screenState) {
        ScreenState.LOADING -> {
            ProgressLoader()
        }

        ScreenState.EMPTY -> {
            ErrorMessage(errorMessage = stringResource(id = AppString.no_data_available))
        }

        ScreenState.ERROR -> {
            ErrorMessage(
                errorMessage = stringResource(id = AppString.something_went_wrong),
                error = true
            )
        }

        ScreenState.DEFAULT -> {
            val context = LocalContext.current
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            )
            {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Gray),
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        model = uiState.userImage,
                        contentDescription = "image",
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(id = AppDrawable.pic)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = uiState.repositoryName ?: "no title",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = uiState.repositoryDescription ?: "no description",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    InfoItem(label = stringResource(id = AppString.star), value = uiState.stars)
                    InfoItem(label = stringResource(id = AppString.forks), value = uiState.forks)
                    InfoItem(label = stringResource(id = AppString.issues), value = uiState.issues)
                    InfoItem(
                        label = stringResource(id = AppString.watchers),
                        value = uiState.watchers
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Project Link:",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = uiState.projectLink ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        openUrl(uiState.projectLink, context)
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Last Updated:",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = uiState.lastUpdated,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Contributors:",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))

                uiState.contributors.forEach { contributor ->
                    Text(text = "• ${contributor.name}", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }

}

@Composable
fun InfoItem(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

fun openUrl(url: String?, context: Context) {
    url?.let {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(it)
        )
        context.startActivity(intent)
    }
}

@Preview(showBackground = true)
@Composable
fun RepositoryDetailPreview() {
    RepositoryDetailScreenContent(
        modifier = Modifier,
        uiState = RepositoryDetailScreenUiState()
    )
}