package com.orukunnn.shapesnapapp.ui.posts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.orukunnn.shapesnapapp.ui.common.LoadState
import com.orukunnn.shapesnapapp.ui.common.ShapeSnapRouteAppBar
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import shapesnapv3.composeapp.generated.resources.Res
import shapesnapv3.composeapp.generated.resources.posts_empty
import shapesnapv3.composeapp.generated.resources.posts_load_more
import shapesnapv3.composeapp.generated.resources.posts_title

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostsScreen(
    viewModel: PostsScreenViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            ShapeSnapRouteAppBar(
                title = stringResource(Res.string.posts_title),
            )
        },
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
        ) {
            when (val s = state) {
                LoadState.Idle, LoadState.Loading -> {
                    Column(
                        Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is LoadState.Error -> Text(s.message, color = MaterialTheme.colorScheme.error)
                is LoadState.Success -> {
                    if (s.data.isEmpty()) {
                        Text(stringResource(Res.string.posts_empty))
                    } else {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(s.data, key = { it.id }) { post ->
                                Card(Modifier.fillMaxWidth()) {
                                    Column(Modifier.padding(12.dp)) {
                                        Text(post.title, style = MaterialTheme.typography.titleMedium)
                                        Text(post.body, style = MaterialTheme.typography.bodyMedium)
                                    }
                                }
                            }
                            item {
                                Button(onClick = { viewModel.loadMore() }, modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                                    Text(stringResource(Res.string.posts_load_more))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}