package com.valeriyu.generic

class TQueue<T>() {
    private var q = mutableListOf<T>()

    fun enqueue(item:T) {
        q.add(item)
        println(q)
    }

    fun dequeue():T? {
        if (q.size == 0) return null
        // Ваш код я закомментировал. А простой вариант предоставил!
//        var r = q[0]
//        q = q.takeLast(q.size -1).toMutableList()
//        return r
        return q.removeAt(0)
    }
}