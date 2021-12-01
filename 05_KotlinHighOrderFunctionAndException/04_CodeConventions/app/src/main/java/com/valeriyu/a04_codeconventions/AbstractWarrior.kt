// package com.valeriyu.module4
package com.valeriyu.a03_exceptions

import kotlin.random.Random
import kotlin.random.nextInt

abstract class AbstractWarrior(
    maxHealth: Int, // максимальный уровень здоровья
    override var chanceToAvoidBeingHit: Int, // шанс избежать попадания
    var hitProbability: Int, // вероятность попадания
    var weapon: AbstractWeapon
) : InterfaceWarrior {

//    private var _maxHealth:Int = maxHealth
//        get() = _maxHealth

    public var currentHealth: Int
    override var isKilled: Boolean = false

    init {
        currentHealth = maxHealth
        weapon.recharge()
    }

    override fun takeDamage(damage: Int) {
        currentHealth = if (currentHealth - damage > 0) currentHealth - damage else 0
        if (currentHealth <= 0) isKilled = true
    }

    // Amno, тип стрельбы и длинна очереди выбираются случайным образом
    override fun attack(war: AbstractWarrior?) {

        if (war!!.isKilled || this!!.isKilled) return

        if (weapon.isEmpty) {
            throw NoAmmoException("Нет патронов !!!")
            // weapon.recharge()
            return
        }

        var totalDamage = 0
        var ft = weapon.fireType
        var ftName = ""
        var rnd = Random.nextInt(100)
        when {
            rnd < 50 -> ft = FireType.Single()
            rnd >= 50 -> ft = FireType.Bursts()
        }

        var amList = mutableListOf<Ammo>()
        // var rrr = IntRange(1, weapon.maxNumberOfRounds)
        // var test = Random.nextInt(rrr)

        var queueLength: Int = Random.nextInt(IntRange(1, weapon.maxNumberOfRounds))

        if (ft.equals(FireType.Single())) {
            amList = weapon.getAmnos(1).toMutableList()
            ftName = FireType.Single().ftName
        } else {
            amList = weapon.getAmnos(queueLength).toMutableList()
            ftName = FireType.Bursts().ftName
        }

        if (amList.count() == 0) {
            println("Пустой список патронрв !!!")
        }
        for (el in amList) {
            if (Random.nextInt(100) < (hitProbability - war.chanceToAvoidBeingHit)) totalDamage = + el.getCarrentDamage()
        }
        if (__DEBUG) println("totalDamage = > $totalDamage FireType => $ftName(${amList.count()}), ${amList[0]} Остаток патронов: ${weapon.ammoList.count()} ")
        if (totalDamage > 0) {
            war.takeDamage(totalDamage)
        }
    }
}
