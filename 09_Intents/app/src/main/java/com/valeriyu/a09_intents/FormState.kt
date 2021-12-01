package com.valeriyu.a09_intents

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FormState(val valid: Boolean = true, var message: String = "") : Parcelable {

}
