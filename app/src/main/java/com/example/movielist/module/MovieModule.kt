package com.example.movielist.module

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.Room
import com.example.movielist.data.MovieList
import com.example.movielist.data.source.local.roomdb.MovieDatabase
import com.example.movielist.remote.MovieApi
import com.example.movielist.repository.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object MovieModule {

    @Singleton
    @Provides
    fun provideMyApi() : MovieApi{
        val client = OkHttpClient.Builder().addInterceptor { chain ->
            val newRequest: Request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI0Y2Q3MzU2NDhmOTYwY2YyZmM0YmE3NTZmNDZkNjkzNSIsInN1YiI6IjY1YWEzZjAwZDk1NDIwMDBjYzIxNDQwZCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.BhjOo0Ihi987d0N-AVM3H9glxvFNuxL56VrNXECYlTM")
                .build()
            chain.proceed(newRequest)
        }.build()

        return Retrofit.Builder()
            .client(client)
            .baseUrl("https://api.themoviedb.org/3/movie/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieApi::class.java)
    }

    @Singleton
    @Provides
    fun provideMyRepository(movieApi: MovieApi, movieDatabase: MovieDatabase, @ApplicationContext appContext: Context) : MovieRepository{
        return MovieRepository(movieApi,movieDatabase)
    }

    @Singleton
    @Provides
    fun provideUserDatabase(@ApplicationContext context: Context): MovieDatabase {
        return Room.databaseBuilder(
            context,
            MovieDatabase::class.java,
            "movieDatabase.db"
        ).build()
    }

    @OptIn(ExperimentalPagingApi::class)
    @Provides
    @Singleton
    fun provideBeerPager(movieDatabase: MovieDatabase, movieApi: MovieApi): Pager<Int, MovieList> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = MovieRepository(
                movieDatabase = movieDatabase,
                movieApi = movieApi
            ),
            pagingSourceFactory = {
                movieDatabase.movieDao.getMovieList()
            }
        )
    }

}