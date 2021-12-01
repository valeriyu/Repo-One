package com.valeriyu.module4

class SecondСlassWarrior(maxHealth:Int = 133,
                         chanceToAvoidBeingHit:Int = 30,
                         hitProbability:Int = 70,
                         weapon:AbstractWeapon = Weapons.WeaponM2):
    AbstractWarrior(maxHealth,
        chanceToAvoidBeingHit,
        hitProbability,
        weapon){
    val className:String ="SecondСlassWarrior"
}