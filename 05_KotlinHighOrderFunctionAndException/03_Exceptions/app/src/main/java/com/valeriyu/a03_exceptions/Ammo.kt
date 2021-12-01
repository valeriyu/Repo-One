//package com.valeriyu.module4
package com.valeriyu.a03_exceptions

import kotlin.random.Random

enum class Ammo (val damage:Int, val chanceOfCriticalDamage:Int, val criticalDamageRatio:Int ) {

    CAT(damage = 30, chanceOfCriticalDamage = 80, criticalDamageRatio = 30),
    DOG(damage = 50, chanceOfCriticalDamage = 60, criticalDamageRatio = 50),
    BEAR(damage = 80, chanceOfCriticalDamage = 40, criticalDamageRatio = 80),
    MOUSE(damage = 100, chanceOfCriticalDamage = 20, criticalDamageRatio = 100),
    EMPTY(damage = 0, chanceOfCriticalDamage = 0, criticalDamageRatio = 0)
    ;

    private var carrentDamage: Int

    init {
            carrentDamage = damage
    }

    fun getCarrentDamage(): Int {
        if (Random.nextInt(100) < chanceOfCriticalDamage) {
            carrentDamage = damage + criticalDamageRatio
        } else carrentDamage = damage
        return carrentDamage
    }
}
