package com.valeriyu.highorderfunctions

fun main(){

    var queue = TQueue<Int>()

    for (i in 0..10){
        queue.enqueue(i)
    }

    var res = queue.filter {
        queue.q = queue.q.filter {  (it?.toInt() % 2) == 0 }.toMutableList()
        //println(queue.q)
    }

    println(res.q.toMutableList())

}