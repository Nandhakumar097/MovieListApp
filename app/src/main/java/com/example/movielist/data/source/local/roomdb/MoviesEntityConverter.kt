package com.example.movielist.data.source.local.roomdb

import androidx.room.TypeConverter
import com.example.movielist.data.MovieList
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class MoviesEntityConverter {

    @TypeConverter
    fun fromStringToMovieList(value: String): List<MovieList>? =
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
            .adapter<List<MovieList>>(Types.newParameterizedType(List::class.java, MovieList::class.java))
            .fromJson(value)

    @TypeConverter
    fun fromMovieListTypeToString(movieListType: List<MovieList>?): String =
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
            .adapter<List<MovieList>>(Types.newParameterizedType(List::class.java, MovieList::class.java))
            .toJson(movieListType)
}