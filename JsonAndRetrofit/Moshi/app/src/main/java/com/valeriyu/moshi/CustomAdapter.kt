package com.valeriyu.moshi

import com.squareup.moshi.*
import java.lang.NullPointerException

class CustomAdapter {

    @FromJson
    fun fromJson(customMovie: CustomMovie): Movie {

        var res = Movie(
            title = customMovie.title,
            year = customMovie.year,
            rated = customMovie.rated,
            genre = customMovie.genre,
            poster = customMovie.poster
        )

        customMovie.scores.map {
            res.scores.plusAssign(it.source to it.value)
        }
        return res
    }

        @JsonClass(generateAdapter = true)
        data class CustomMovie(
            @Json(name = "Title")
            val title: String = "",
            @Json(name = "Year")
            val year: Int? = null, //Int
            @Json(name = "Rated")
            val rated: MovieRated = MovieRated.GENERAL,
            @Json(name = "Genre")
            val genre: String = "",
            @Json(name = "Poster")
            val poster: String = "",
            @Json(name = "Ratings")
            val scores: List<Score> = emptyList()
        )
    }
