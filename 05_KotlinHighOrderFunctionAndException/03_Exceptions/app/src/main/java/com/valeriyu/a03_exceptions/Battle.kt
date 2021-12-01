//package com.valeriyu.module4
package com.valeriyu.a03_exceptions

import kotlin.random.Random

class Battle(var commandA:Team, var commandB:Team, var qntRaunds:Int = 40) {

    private var battleIsOver: Boolean

    init {
        battleIsOver = false
    }

    private fun getBattleState():BattleState {
        //var bs = BattleState
        var res = ""
        var commandAHealth: Int = 0
        var cntA: Int = 0
        for (el in commandA.listWarriors) {
            commandAHealth += el!!.currentHealth
            if (!el.isKilled) cntA++
        }

        var cntB: Int = 0
        var commandBHealth: Int = 0
        for (el in commandB.listWarriors) {
            commandBHealth += el!!.currentHealth
            if (!el.isKilled) cntB++
        }

        if (cntA == 0 || cntB == 0) battleIsOver = true

        //println("(commandAHealth = $commandAHealth, numWarriors = $cntA, commandBHealth = $commandBHealth, numWarriors = $cntB)")
        res = "(commandAHealth = $commandAHealth, numWarriors = $cntA, commandBHealth = $commandBHealth, numWarriors = $cntB)"
        //println(progress)

        if (cntA == 0 && cntB == 0) {
            println("(commandAHealth = $commandAHealth, numWarriors = $cntA, commandBHealth = $commandBHealth, numWarriors = $cntB)")
            return BattleState.Draw
        }
        if (cntA == 0) {
            println("(commandAHealth = $commandAHealth, numWarriors = $cntA, commandBHealth = $commandBHealth, numWarriors = $cntB)")
            return BattleState.victoryTeamB
        }
        if (cntB == 0){
            println("(commandAHealth = $commandAHealth, numWarriors = $cntA, commandBHealth = $commandBHealth, numWarriors = $cntB)")
            return BattleState.victoryTeamA
        }

        //println(res)
        BattleState.Progress.res = res
        // BattleState.Progress.res + "(commandAHealth = $commandAHealth  $commandA.listWarriors.count(), commandBHealth = $commandBHealth  commandB.listWarriors.count())"

        return BattleState.Progress
    }

/*
    fun shuffled() {
        var n = commandA.listWarriors.count() - 1
        var lst = (commandA.listWarriors + commandB.listWarriors).shuffled()

        // lst = lst.shuffled()
        commandA.listWarriors = lst.take(n).toMutableList()
        commandB.listWarriors = lst.takeLast(n).toMutableList()
    }
*/
    fun startBattle() {
        var iT: Int = 0
        var jT: Int = 0

        var wA: AbstractWarrior?
        var wB: AbstractWarrior?
        var n = commandA.listWarriors.count()

        var rFinish = false

        var countRouns: Int = qntRaunds
        var sw = true
        var l = listOf(
            commandA.listWarriors.shuffled().toMutableList(),
            commandB.listWarriors.shuffled().toMutableList()
        )

        println("${getBattleState().res}")

        while (countRouns > 0 && !battleIsOver) {
            var txt = if (sw) "A" else "B"
            var txtA = ""
            var txtB = ""
            println("Раунд: ${qntRaunds - countRouns + 1}. Начикает команда $txt.")
            for (i in 0..n - 1 step 2) {

                if (sw) {
                    l = listOf(
                        commandA.listWarriors.shuffled().toMutableList(),
                        commandB.listWarriors.shuffled().toMutableList()
                    )
                    txtA = "WarrA"
                    txtB = "WarrB"
                } else {
                    l = listOf(
                        commandB.listWarriors.shuffled().toMutableList(),
                        commandA.listWarriors.shuffled().toMutableList()
                    )
                    txtB = "WarrA"
                    txtA = "WarrB"
                }

                wA = l[0][i]
                wB = l[1][i]

                if (wA!!.isKilled || wB!!.isKilled) continue
                if(__DEBUG ) println("$txtA [$i] [${wA.currentHealth}] атакует $txtB [$i] [${wB.currentHealth}]")

                try {
                    wA!!.attack(wB)
                }catch(e:NoAmmoException){
                    if(__DEBUG ) println("Нет патронов !!!")
                    wA.weapon.recharge()
                }

                if(__DEBUG ) println("$txtA [$i] [${wA.currentHealth}]  $txtB [$i] [${wB.currentHealth}]")
                if(__DEBUG )println()

                if (!wB.isKilled){
                    if(__DEBUG ) println("$txtB [$i] [${wB.currentHealth}] атакует $txtA [$i] [${wA.currentHealth}]")

                    try {
                        wB!!.attack(wA)
                    }catch(e:NoAmmoException){
                        wB.weapon.recharge()
                    }

                    if(__DEBUG ) println("$txtA [$i] [${wB.currentHealth}]  $txtB [$i] [${wA.currentHealth}]")
                    if(__DEBUG )println()
                }

                wA = l[0][i + 1]
                wB = l[1][i + 1]

                if (wA!!.isKilled || wB!!.isKilled) continue
                if(__DEBUG ) println("$txtB [${i + 1}] [${wB.currentHealth}] атакует $txtA [${i + 1}] [${wA.currentHealth}]")
                try {
                    wB!!.attack(wA)
                }catch(e:NoAmmoException){
                    if(__DEBUG ) println("Нет патронов !!!")
                    wB.weapon.recharge()
                }

                if(__DEBUG ) println("$txtB [${i + 1}] [${wB.currentHealth}]  $txtA [${i + 1}] [${wA.currentHealth}]")
                //println()

                if (!wA.isKilled){
                    if(__DEBUG ) println("$txtA [$i] [${wA.currentHealth}] атакует $txtB [$i] [${wB.currentHealth}]")

                    try {
                        wB!!.attack(wA)
                    }catch(e:NoAmmoException){
                        if(__DEBUG ) println("Нет патронов !!!")
                        wB.weapon.recharge()
                    }

                    if(__DEBUG ) println("$txtA [$i] [${wA.currentHealth}]  $txtB [$i] [${wB.currentHealth}]")
                    if(__DEBUG )println()
                }
            }
            sw = !sw
            println("${getBattleState().res}")
            countRouns--
        }
//        getBattleState()
    }
}