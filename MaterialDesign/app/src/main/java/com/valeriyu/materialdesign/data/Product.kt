package com.valeriyu.materialdesign.data

import androidx.annotation.DrawableRes

data class Product(
    val title: String,
    val secondary_text: String,
    @DrawableRes
    val image: Int
)
