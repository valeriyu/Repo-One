//package com.valeriyu.module4
package com.valeriyu.a03_exceptions

import kotlin.Exception
import kotlin.random.Random
import kotlin.random.nextInt

abstract class AbstractWeapon(var maxNumberOfRounds:Int, var fireType:FireType) {
    var isEmpty:Boolean = true
    var ammoList = mutableListOf<Ammo>()
    var typeAmmo:Ammo = Ammo.EMPTY


    fun createAmno():Ammo{
        typeAmmo = Ammo.values()[ Random.nextInt(IntRange(0, Ammo.values().size - 2))]
        return typeAmmo
    }

    fun recharge(){

        if (ammoList.count() >= maxNumberOfRounds) {
            isEmpty = false
            return
        }

        createAmno()
        for ( i in 0..maxNumberOfRounds - 1){
            ammoList.add(i, typeAmmo)
        }
        isEmpty = false
    }

    fun getAmnos(quan:Int):List<Ammo>{
        if (ammoList.isEmpty()) {
            ammoList.clear()
            return mutableListOf<Ammo>()
        }

        var r = mutableListOf<Ammo>()
        if (quan < ammoList.count()) {
//            r = ammoList.takeLast(quan).toMutableList()
//            ammoList = ammoList.take(ammoList.count() - quan).toMutableList()

            r = ammoList.take(quan).toMutableList()
            ammoList = ammoList.takeLast(ammoList.count() - quan).toMutableList()
        } else{
            r = ammoList.takeLast(ammoList.count()).toMutableList()
//            ammoList = mutableListOf<Ammo>()
            ammoList.clear()
            isEmpty = true
        }
        return r
    }

}