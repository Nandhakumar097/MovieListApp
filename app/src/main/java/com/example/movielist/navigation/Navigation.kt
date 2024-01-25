package com.example.movielist.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.movielist.screens.MovieInfoScreen
import com.example.movielist.screens.MovieListScreen
import com.example.movielist.viewmodel.MovieViewModel

@Composable
fun Navigation(myViewModel: MovieViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.MovieListScreen.route) {
        composable(route = Screen.MovieListScreen.route) {
            MovieListScreen(navController = navController, moviesViewModel = myViewModel)
        }
        composable(route = Screen.MovieInfoScreen.route + "/{movie_id}",
            arguments = listOf(
                navArgument("movie_id") {
                    type = NavType.IntType
                    defaultValue = 0
                    nullable = false
                }
            )
        ) { _ ->
            MovieInfoScreen(moviesViewModel = myViewModel)
        }
    }
}


