package com.valeriyu.moshi

import com.squareup.moshi.Json

enum class MovieRated {
    @Json(name = "G")
    GENERAL,
    PG,
    @Json(name = "PG-13")
    PG_13,
    R,
    @Json(name = "NC-17")
    NC_17,
    @Json(name = "Not Rated")
    Not_Rated
}