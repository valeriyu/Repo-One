package com.valeriyu.a10_fragments

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FormState(val valid: Boolean = true, var message: String = "") : Parcelable {

}
