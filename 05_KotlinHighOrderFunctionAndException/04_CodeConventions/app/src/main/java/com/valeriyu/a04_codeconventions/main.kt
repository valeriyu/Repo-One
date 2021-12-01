// package com.valeriyu.module4
package com.valeriyu.a03_exceptions

const val __DEBUG = true
// const val __DEBUG = false

const val QNT_ROUNDS = 100

fun main() {

    println("""
Введите количество воинов в командах:
 (лучше четное количество)
или Q (q) для выхода.""")

    var buf: String? = ""
    while (true) {
        buf = readLine()

        if (buf == null) {
            println("Странно - Ничего не ввелось !!!")
            return
        } else if (buf == "Q" || buf == "q") {
            return
        } else if (buf.toIntOrNull() == null) {
            println("Это не число - повторите ввод !!!")
            continue
        } else break
    }

    val n: Int = buf!!.toInt()
    var TeamA = Team(n)
    var TeamB = Team(n)

    var bat = Battle(TeamA, TeamB, QNT_ROUNDS)
    bat.startBattle()
}
