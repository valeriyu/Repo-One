//package com.valeriyu.module4
package com.valeriyu.a03_exceptions

interface InterfaceWarrior {
    var isKilled:Boolean
    var chanceToAvoidBeingHit: Int

    fun attack(war: AbstractWarrior?){}
    fun takeDamage(damage:Int)
}