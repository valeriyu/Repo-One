package com.valeriyu.module4

sealed class FireType {

  data class Single(val ftName:String = "Single"):FireType()
  data class Bursts(val ftName:String = "Bursts"):FireType()

//  data class ShortBursts(var ftName:String ="ShortBursts"):FireType()
}


