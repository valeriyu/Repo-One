
package com.valeriyu.moshi


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Movie(
    @Json(name ="Title")
    val title: String = "",
    @Json(name ="Year")
    val year: Int? = null, //Int
    @Json(name ="Rated")
    val rated:  MovieRated = MovieRated.GENERAL,
    @Json(name ="Genre")
    val genre: String = "",
    @Json(name ="Poster")
    val poster: String = ""

    ,
     @Json(name ="Ratings")
    var scores: MutableMap<String,String> = mutableMapOf()
)

/*
3. У фильма должны быть следующие свойства:

a. название (строка),

b. год (число),

c. рейтинг (enum),

d. жанр (строка),

e. постер (URL),

f. оценки (список объектов класса оценки).

У оценки должны быть следующие свойства:

a. источник оценки (строка),
b. оценка (строка).
 */