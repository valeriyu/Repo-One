package com.valeriyu.a11_fragments

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FragmentState(
    var badgePos: Int? = null,
    var checkedItems: BooleanArray = booleanArrayOf(),
    var badgeNumber: Int = 0
) : Parcelable