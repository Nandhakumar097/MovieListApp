package com.example.movielist.remote

import com.example.movielist.data.MovieInfoResult
import com.example.movielist.data.MovieListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {

    @GET("now_playing")
    suspend fun getMovieList(@Query("language") language : String = "en-US", @Query("page") page : Int = 1) : Response<MovieListResponse>

    @GET("{movieId}")
    suspend fun getMovieInfo(@Path("movieId") movieId : Int, @Query("language") language : String = "en-US") : Response<MovieInfoResult>

}