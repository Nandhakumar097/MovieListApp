package com.example.movielist.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import retrofit2.http.GET
import java.io.Serializable

@Entity
data class MovieList(
    var adult: Boolean = false,
    var backdrop_path: String? = "",
    @PrimaryKey
    var id: Int = 0,
    var original_language: String = "",
    var original_title: String = "",
    var overview: String = "",
    var popularity: Double = 0.0,
    var poster_path: String? = "",
    var release_date: String = "",
    var title: String = "",
    var video: Boolean = false,
    var vote_average: Double = 0.0,
    var vote_count: Int = 0
) : Serializable {
    fun doesMatchSearchQuery(query : String) : Boolean{
        val matchingCombinations = listOf(
            title.replace(" ",""),
            "$title")
        return matchingCombinations.any {
            it.contains(query,ignoreCase = true)
        }
    }

}
