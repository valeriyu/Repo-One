package com.valeriyu.module4

class FourthСlassWarrior(maxHealth:Int = 100,
                         chanceToAvoidBeingHit:Int = 10,
                         hitProbability:Int = 50,
                         weapon:AbstractWeapon = Weapons.WeaponM4):
    AbstractWarrior(maxHealth,
        chanceToAvoidBeingHit,
        hitProbability,
        weapon){
    val className:String ="FourthСlassWarrior"
}