package com.valeriyu.module4

class ThirdСlassWarrior(maxHealth:Int = 166,
                        chanceToAvoidBeingHit:Int = 20,
                        hitProbability:Int = 60,
                        weapon:AbstractWeapon = Weapons.WeaponM3):
    AbstractWarrior(maxHealth,
        chanceToAvoidBeingHit,
        hitProbability,
        weapon) {
    val className:String ="ThirdСlassWarrior"
}