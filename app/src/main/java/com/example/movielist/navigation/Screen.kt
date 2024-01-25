package com.example.movielist.navigation

sealed class Screen(var route: String){
    object MovieListScreen: Screen("movie_list_screen")
    object MovieInfoScreen: Screen("movie_info_screen")

    fun withArg(vararg args: Int): String{
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }

}
