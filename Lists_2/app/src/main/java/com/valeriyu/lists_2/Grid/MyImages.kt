package com.valeriyu.lists_2.Grid

sealed class MyImages {
    data class Image(
        var imageUrl: String
    ): MyImages()
}