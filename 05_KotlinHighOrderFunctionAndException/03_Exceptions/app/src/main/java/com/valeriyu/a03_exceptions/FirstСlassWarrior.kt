//package com.valeriyu.module4
package com.valeriyu.a03_exceptions

class FirstСlassWarrior(maxHealth:Int = 200,
chanceToAvoidBeingHit:Int = 40,
hitProbability:Int = 80,
weapon:AbstractWeapon = Weapons.WeaponM1
//                                 className:String
):
AbstractWarrior(maxHealth,
chanceToAvoidBeingHit,
hitProbability,
weapon){
    val className:String = "FirstСlassWarrior"
}