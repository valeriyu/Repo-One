// package com.valeriyu.module4
package com.valeriyu.a03_exceptions

sealed class FireType {

    data class Single(val ftName: String = "Single") : FireType()
    data class Bursts(val ftName: String = "Bursts") : FireType()

//  data class ShortBursts(var ftName:String ="ShortBursts"):FireType()
}
