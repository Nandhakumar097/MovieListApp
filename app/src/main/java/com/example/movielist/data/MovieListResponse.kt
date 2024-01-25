package com.example.movielist.data

import java.io.Serializable

data class MovieListResponse(
        var dates: Dates = Dates(),
        var page: Int = 0,
        var results: List<MovieList> = arrayListOf(),
        var total_pages: Int = 0,
        var total_results: Int = 0
) : Serializable

data class MovieUiState(
        val moviesList: List<MovieList> = emptyList(),
) : Serializable