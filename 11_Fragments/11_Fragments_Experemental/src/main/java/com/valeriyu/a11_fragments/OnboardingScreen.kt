package com.valeriyu.a11_fragments

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class OnboardingScreen(
    @StringRes val textRes: Int,
    @StringRes val textTabRes: Int,
    @DrawableRes val drawableRes: Int,
    var tags: List<ArticleTag>?
)