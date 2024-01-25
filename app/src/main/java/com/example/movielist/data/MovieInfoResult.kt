package com.example.movielist.data

data class MovieInfoResult(
        var adult: Boolean = false,
        var backdrop_path: String = "",
        var belongs_to_collection: BelongsToCollection = BelongsToCollection(),
        var budget: Int = 0,
        var genres: List<Genre> = arrayListOf(),
        var homepage: String = "",
        var id: Int = 0,
        var imdb_id: String = "",
        var original_language: String = "",
        var original_title: String = "",
        var overview: String = "",
        var popularity: Double = 0.0,
        var poster_path: String = "",
        var production_companies: List<ProductionCompany> = arrayListOf(),
        var production_countries: List<ProductionCountry> = arrayListOf(),
        var release_date: String = "",
        var revenue: Int = 0,
        var runtime: Int = 0,
        var spoken_languages: List<SpokenLanguage> = arrayListOf(),
        var status: String = "",
        var tagline: String = "",
        var title: String = "",
        var video: Boolean = false,
        var vote_average: Double = 0.0,
        var vote_count: Int = 0
)