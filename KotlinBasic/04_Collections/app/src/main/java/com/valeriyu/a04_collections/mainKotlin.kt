package com.valeriyu.a04_collections

fun main() {
    println(
        """
Введите количество номеров: 
или Q (q) для выхода."""
    )

    var buf: String?
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
    var phones = inputPhonesNums(n)

    println("Номера содержащие +7: ${phones.filter { it.contains("+7") }}")
    println("Количество уникальных введенных номеров: ${phones.toSet().size}")
    println("Сумма длин всех номеров телефонов: ${phones.map { it.length }.sumBy { it }}")

    var mmap = mutableMapOf<String, String>()

    phones.forEach {
        println("Введите имя человека с номером телефона ${it} : ")
        val name = readLine()
        if (name == null) {
            println("Странно - Ничего не ввелось !!!")
        } else {
            mmap.put(it, name)
        }
    }

    //println("${mmap}")

    mmap.forEach {
        println("Человек: ${it.value}. Номер телефона: ${it.key}.")
    }
}

fun inputPhonesNums(n: Int): List<String> {
    val myList = mutableListOf<String>()

    var count = 0
    while (count < n) {
        println("Введите номер телефона № ${count + 1}: ")
        val buf = readLine()
        if (buf == null) {
            println("Странно - Ничего не ввелось !!!")
            continue
        } else {
            myList.add(count, buf)
            count++
            continue
        }
    }
    return myList
}
