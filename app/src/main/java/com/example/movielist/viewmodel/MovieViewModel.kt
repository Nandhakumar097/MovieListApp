package com.example.movielist.viewmodel

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.example.movielist.connectivity.ConnectivityObserver
import com.example.movielist.connectivity.NetworkConnectivityObserver
import com.example.movielist.data.MovieInfoResult
import com.example.movielist.data.MovieList
import com.example.movielist.remote.Resource
import com.example.movielist.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
        pager: Pager<Int, MovieList>,
        private val movieRepository: MovieRepository,
        @ApplicationContext private val context: Context
) : ViewModel() {

    private val _infoState = mutableStateOf(MovieInfoResult())
    val infoState: State<MovieInfoResult> = _infoState
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()
    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()
    lateinit var connectivityObserver: ConnectivityObserver

    init {
        getNetworkStatus()
    }

    private fun getNetworkStatus() {
        connectivityObserver = NetworkConnectivityObserver(context)
    }

    val pagingFlow = pager.flow
        .map { pagingData ->
            pagingData.map { it }.filter { it.doesMatchSearchQuery(searchText.value) }
        }.cachedIn(viewModelScope)
        .onEach { _isSearching.update { true } }
        .combine(searchText) { movies, text ->
            if (text.isBlank()) {
                movies
            } else {
                movies.filter {
                    it.doesMatchSearchQuery(text)
                }
            }
        }.onEach { _isSearching.update { false } }

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun getMoviesInfo(movieId: Int) {
        viewModelScope.launch {
            movieRepository.getMovieInfo(movieId).onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        _infoState.value = MovieInfoResult()
                    }

                    is Resource.Success -> {
                        _infoState.value = result.data ?: MovieInfoResult()
                    }

                    is Resource.Error -> {
                        _infoState.value = MovieInfoResult()
                    }
                }
            }.launchIn(this)
        }
    }

    fun onToogleSearch() {
        _isSearching.value = !_isSearching.value
        if (!_isSearching.value) {
            onSearchTextChange("")
        }
    }

}