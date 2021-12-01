package com.valeriyu.module3

// import java.util.stream.LongStream


fun main(){
        println("""
Введите количество чисел: 
или Q (q) для выхода.""")

    var buf:String? = ""
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

    val n:Int = buf!!.toInt()
    var lst = inputNum(n)

    println()
    print("Введены Числа: [ ")
    var count = 0
    for (el in lst) {
        if(el!!.toInt() > 0) {print("$el ") ; count++}
    }
    println("]")
    println("Введено $count положительных чисел(а).")

    print("Четные числа: ")
    println(lst.filter {  (it!!.toInt() % 2) == 0 })

    println("Количество уникальных введенных чисел: ${lst.toSet().size}")

    var sum = lst.sum()
    println("Сумма чисел: $sum")

    println()
    println("===================== Результат =====================")

    var map = mutableMapOf<Int, Int>()
    for (el in lst) {
       map.put(el, gcd(el, sum))
       println("$el, $sum, ${gcd(el, sum)}")
       //print(gcd(el, sum))
            }
    println("===================== Map =====================")
    println(map)
}

fun inputNum(n: Int):List<Int> {
    val myList = mutableListOf<Int>()

    var count = 0
    while (count < n) {
        println("Введите число № ${count + 1}: ")
        var buf = readLine()
        if (buf == null) {
            println("Странно - Ничего не ввелось !!!")
            continue
        } else if (buf.toIntOrNull() == null) {
            println("Это не число - повторите ввод !!!")
            continue
        } else {
        myList.add(count, buf!!.toInt())
        count++
        continue}
    }
    return myList
}

tailrec fun gcd(a:Int, b:Int):Int{
    if (a == b) return a;
    if (a == 0) return b;
    if (b == 0) return a;
    return gcd(b, a % b);
}

/*
unsigned int gcd(unsigned int u, unsigned int v)
{
    // simple cases (termination)
    if (u == v)
        return u;

    if (u == 0)
        return v;

    if (v == 0)
        return u;

    // look for factors of 2
    if (~u & 1) // u is even
    if (v & 1) // v is odd
    return gcd(u >> 1, v);
    else // both u and v are even
    return gcd(u >> 1, v >> 1) << 1;

    if (~v & 1) // u is odd, v is even
    return gcd(u, v >> 1);

    // reduce larger argument
    if (u > v)
        return gcd((u - v) >> 1, v);

    return gcd((v - u) >> 1, u);
}
*/

