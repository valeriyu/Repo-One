// package com.valeriyu.module4
package com.valeriyu.a03_exceptions

object Weapons {
    object WeaponM1 : AbstractWeapon(10, FireType.Single())
    object WeaponM2 : AbstractWeapon(20, FireType.Single())
    object WeaponM3 : AbstractWeapon(30, FireType.Single())
    object WeaponM4 : AbstractWeapon(40, FireType.Bursts())
}
