package com.valeriyu.module4

interface InterfaceWarrior {
    var isKilled:Boolean
    var chanceToAvoidBeingHit: Int

    fun attack(war: AbstractWarrior?){}
    fun takeDamage(damage:Int)
}