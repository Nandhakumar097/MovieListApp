package com.example.movielist.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.TypeConverters
import androidx.room.withTransaction
import com.example.movielist.data.MovieInfoResult
import com.example.movielist.data.MovieList
import com.example.movielist.data.source.local.roomdb.MovieDatabase
import com.example.movielist.data.source.local.roomdb.MoviesEntityConverter
import com.example.movielist.remote.MovieApi
import com.example.movielist.remote.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import kotlin.math.roundToInt

@OptIn(ExperimentalPagingApi::class)
@TypeConverters(MoviesEntityConverter::class)
class MovieRepository @Inject constructor(
        private val movieApi: MovieApi,
        private val movieDatabase: MovieDatabase
) : RemoteMediator<Int, MovieList>() {


    fun getMovieInfo(movieId: Int): Flow<Resource<MovieInfoResult>> = flow {
        try {
            emit(Resource.Success(movieApi.getMovieInfo(movieId = movieId).body()))
        } catch (ex: Exception) {
            emit(
                Resource.Error(
                    message = "Oops, something went wrong!"
                )
            )
        }
    }

    override suspend fun load(
            loadType: LoadType,
            state: PagingState<Int, MovieList>
    ): MediatorResult {
        return try {
            val loadKey : Int = when(loadType){
                LoadType.REFRESH -> {
                    1
                }
                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    if(lastItem == null) {
                        1
                    } else if (movieDatabase.movieDao.getCount() >= 100)
                        return MediatorResult.Success(endOfPaginationReached = true)
                    else {
                        (movieDatabase.movieDao.getCount() / state.config.pageSize.toDouble()).roundToInt().plus(1)
                    }
                }
            }

            val remoteMovies = movieApi.getMovieList(page = loadKey)

            movieDatabase.withTransaction {
                if (loadType == LoadType.REFRESH){
                    movieDatabase.movieDao.deleteAll()
                }
                remoteMovies.body()?.results?.let {
                    try {
                        movieDatabase.movieDao.insertMovieList(it)
                    }catch (ex : Exception){
                        println(ex)
                    }
                }
            }
            MediatorResult.Success(
                endOfPaginationReached = remoteMovies.body()?.results?.isEmpty() ?: true
            )
        }catch (e: IOException){
            MediatorResult.Error(e)
        }catch (e : HttpException){
            MediatorResult.Error(e)
        }
    }

}