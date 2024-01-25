package com.example.movielist.data.source.local.roomdb.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movielist.data.MovieList

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieList(movies: List<MovieList>)

    @Query("SELECT * FROM MovieList")
    fun getMovieList(): PagingSource<Int, MovieList>

    @Query("DELETE FROM MovieList")
    suspend fun deleteAll()

    @Query("SELECT * FROM MovieList where id = :movieId")
    suspend fun getMovieInfo(movieId: Int): MovieList

    @Query("SELECT COUNT(id) FROM MovieList")
    suspend fun getCount(): Int

}