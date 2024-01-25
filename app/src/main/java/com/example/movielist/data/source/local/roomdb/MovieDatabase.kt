package com.example.movielist.data.source.local.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.movielist.data.MovieList
import com.example.movielist.data.source.local.roomdb.dao.MovieDao

@Database(
    entities = [MovieList::class],
    version = 1
)
@TypeConverters(MoviesEntityConverter::class)
abstract class MovieDatabase : RoomDatabase(){

    abstract val movieDao : MovieDao

}