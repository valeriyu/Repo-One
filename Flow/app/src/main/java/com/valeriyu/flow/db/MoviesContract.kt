package com.valeriyu.flow.db

import com.squareup.moshi.Json

object MoviesContract {
    const val TABLE_NAME = "movies"

    object Columns {
        const val ID = "id"
        const val imdbID = "imdbID"
        const val TITLE = "title"
        const val YEAR = "year"
        const val TUPE = "type"
        const val POSTER = "poster"
    }
}