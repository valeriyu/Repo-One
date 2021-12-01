package com.valeriyu.module4

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
        createAmno()
        for ( i in 0..maxNumberOfRounds - 1){
            ammoList.add(i, typeAmmo)
        }
        isEmpty = false
    }

    fun getAmnos(quan:Int):List<Ammo>{
        if (ammoList.isEmpty()) return  mutableListOf<Ammo>()

        var r = mutableListOf<Ammo>()
        if (quan < ammoList.count()) {
            r = ammoList.takeLast(quan).toMutableList()
            ammoList = ammoList.take(ammoList.count() - quan).toMutableList()
        } else{
            r = ammoList.takeLast(ammoList.count()).toMutableList()
            ammoList = mutableListOf<Ammo>()
            isEmpty = true
        }
        return r
    }

}