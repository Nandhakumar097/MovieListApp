package com.example.movielist.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.movielist.connectivity.ConnectivityObserver
import com.example.movielist.data.Constant
import com.example.movielist.data.MovieList
import com.example.movielist.navigation.Screen
import com.example.movielist.ui.theme.Red20
import com.example.movielist.viewmodel.MovieViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieListScreen(navController: NavHostController, moviesViewModel: MovieViewModel) {
    val searchText by moviesViewModel.searchText.collectAsState()
    val isSearching by moviesViewModel.isSearching.collectAsState()
    val movies = moviesViewModel.pagingFlow.collectAsLazyPagingItems()
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = false)
    val isOnline by moviesViewModel.connectivityObserver.observe()
        .collectAsState(initial = ConnectivityObserver.Status.Unavailable)

    SwipeRefresh(state = swipeRefreshState, onRefresh = {
        movies.refresh()
    }) {
        Scaffold(
            topBar = {
                SearchBar(
                    query = searchText,
                    onQueryChange = moviesViewModel::onSearchTextChange,
                    onSearch = moviesViewModel::onSearchTextChange,
                    active = false,
                    placeholder = {
                        Row {
                            Icon(
                                Icons.Filled.Search, contentDescription = null,
                                modifier = Modifier.padding(start = 5.dp)
                            )
                            Text(
                                text = "Search Movie",
                                modifier = Modifier.padding(start = 10.dp)
                            )
                        }

                    },
                    onActiveChange = {
                        moviesViewModel.onToogleSearch()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                ) {

                }
            }
        ) { value ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(value)
            ) {

                if (isSearching) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                } else {
                    if (isOnline != ConnectivityObserver.Status.Available) {
                        Text(
                            text = "Network Connection - ${isOnline.name}",
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.error),
                            color = MaterialTheme.colorScheme.onError,
                            textAlign = TextAlign.Center
                        )
                    }
                    if (movies.itemCount == 0){
                        NoData()
                    }else
                    LazyColumn(
                        Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        items(movies.itemCount) { i ->
                            movies[i]?.let {
                                MovieItem(
                                    it,
                                    moviesViewModel,
                                    navController,
                                    isOnline == ConnectivityObserver.Status.Available
                                )
                            }
                        }
                    }
                }
            }
        }

    }


}

@Composable
fun MovieItem(
        movieList: MovieList,
        moviesViewModel: MovieViewModel,
        navController: NavHostController,
        isOnline: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                if (isOnline) {
                    moviesViewModel.getMoviesInfo(
                        movieList.id
                    )
                    navController.navigate(
                        Screen.MovieInfoScreen.withArg(
                            movieList.id
                        )
                    )
                }
            },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Box(
            modifier = Modifier,
        ) {

            AsyncImage(
                model = Constant().LOGO_URL + movieList.backdrop_path,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .blur(1.dp),
            )

            Text(
                text = movieList.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
                )

            Card(
                modifier = Modifier
                    .padding(15.dp)
                    .align(Alignment.TopStart),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                ),
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Text(
                    text = if (movieList.adult) "A" else "U",
                    modifier = Modifier
                        .padding(start = 7.dp, end = 7.dp, top = 3.dp, bottom = 3.dp),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (movieList.adult) Red20 else MaterialTheme.colorScheme.secondary
                )
            }


            Card(
                modifier = Modifier
                    .padding(top = 5.dp, end = 10.dp)
                    .align(Alignment.TopEnd),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                ),
                shape = MaterialTheme.shapes.medium,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.background),
            ) {
                AsyncImage(
                    model = Constant().BACKDROP_URL + movieList.poster_path,
                    contentDescription = null,
                    modifier = Modifier
                        .width(120.dp)
                        .align(Alignment.End)
                )
            }
        }
    }
}

@Composable
fun NoData() {
    Text(
        text = "No Data Available",
        modifier = Modifier.fillMaxSize(),
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.error
    )
}
