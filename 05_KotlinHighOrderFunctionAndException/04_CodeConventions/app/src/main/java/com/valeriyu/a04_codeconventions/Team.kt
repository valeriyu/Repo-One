// package com.valeriyu.module4
package com.valeriyu.a03_exceptions

import kotlin.random.Random

class Team(var numWarriors: Int) {
    var listWarriors = mutableListOf<AbstractWarrior?>()

    // класс 1 вер. 10%
    // класс 2 вер. 20%
    // класс 3 вер. 40%
    // класс 4 вер. 60%
    init {
        crTeam(numWarriors)
        val livСoun: Int = numWarriors
    }

    private fun crTeam(n: Int) {
        var p = 0F
        var k = 100F / 13
        var wList = listOf(Weapons.WeaponM1, Weapons.WeaponM2, Weapons.WeaponM3, Weapons.WeaponM4)

        for (i in 0..numWarriors - 1) {
            var w = wList[Random.nextInt(3)]

            p = Random.nextFloat() * 100
            if (p < k) {
                listWarriors.add(i, FirstСlassWarrior(weapon = w))
            } // 10%
            if (p >= k && p < 2 * k) {
                listWarriors.add(i, SecondСlassWarrior(weapon = w))
            } // 20
            if (p >= 2 * k && p < 4 * k) {
                listWarriors.add(i, ThirdСlassWarrior(weapon = w))
            } // 40
            if (p >= 4 * k) {
                listWarriors.add(i, FourthСlassWarrior(weapon = w))
            } // 60
        }
    }
}
