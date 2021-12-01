package com.valeriyu.generic

fun <T:Number> genericFun(p:List<out T>):List<T> {
    //println(p.filter {  (it?.toInt() % 2) == 0 })
    return (p.filter {  (it?.toInt() % 2) == 0 })
}

fun task1(){

    var test = IntRange(0, 10).toList()
    var test1 = listOf<Double>(0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0 ,7.0, 8.0, 9.0, 10.0)

    println(genericFun(test))
    println(genericFun(test1))
}

//=========================================================================================

fun task2(){
    var Q = TQueue<Any>()
    Q.enqueue(123E2)
    Q.enqueue("Это строка !!!")
    Q.enqueue(321.0)
    println()
    println(Q.dequeue())
    println(Q.dequeue())
    println(Q.dequeue())
    println(Q.dequeue())
}

//=========================================================================================


fun task3(){
    var res:Result<Int, String> = getResult(1)
    println("Param = 1: $res")

    res = getResult(-1)
    println("Param = -1: $res")

}
//=========================================================================================

fun main() {
    println("   Задача №1")
    task1()

    println("   Задача №2")
    task2()

    println("   Задача №3")
    task3()
}








